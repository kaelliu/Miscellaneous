require "luasql.oci8"

conn_str = [[
(DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = 10.188.54.72)(PORT = 1521))
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = hsmis)
    )
)
]]

env = assert(luasql.oci8())
con = assert(env:connect(conn_str, "uss", "uss"))

-- 创建 TUserInfo 表，即用户信息表。
-- user_id 用户号，来自海晟。
-- user_type 用户类别，取值如下：
--   消费者='X'
--   零售户='L'
--   客户经理='J'
--   客服='K'
--   工业公司='G'
--   商业公司='S'
--   未知='U'
-- user_name 用户名。
-- sex 性别。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_tables where table_name='TUSERINFO';
		if (a = 0) then
			execute immediate ('
				CREATE TABLE TUserInfo(
					user_id varchar2(10) not null,
					user_type varchar2(1) not null,
					user_name varchar2(16) not null,
					sex varchar2(2) not null)
			');
			execute immediate ('
				ALTER TABLE TUserInfo ADD CONSTRAINT TUserInfo_PK PRIMARY KEY (
					user_id)
			');
			execute immediate ('
				CREATE INDEX TUserInfo_Index_1 ON TUserInfo (user_name)
			');
		end if;
	end;
	]]))

-- 创建 TFriend 表，即好友表。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_tables where table_name='TFRIEND';
		if (a = 0) then
			execute immediate ('
				CREATE TABLE TFriend(
					user_id varchar2(10) not null,
					other_user_id varchar2(10) not null)
			');
			execute immediate ('
				ALTER TABLE TFriend ADD CONSTRAINT TFriend_PK PRIMARY KEY (
					user_id, other_user_id)
			');
		end if;
	end;
	]]))

-- 创建 TShield 表，即黑名单表，即屏蔽表。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_tables where table_name='TSHIELD';
		if (a = 0) then
			execute immediate ('
				CREATE TABLE TShield(
					user_id varchar2(10) not null,
					other_user_id varchar2(10) not null)
			');
			execute immediate ('
				ALTER TABLE TShield ADD CONSTRAINT TShield_PK PRIMARY KEY (
					user_id, other_user_id)
			');
		end if;
	end;
	]]))

-- 创建用于 TChat 表的 id 字段的 Sequence。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_sequences where sequence_name='TCHATIDSEQ';
		if (a = 0) then
			execute immediate ('
				CREATE SEQUENCE TChatIdSeq
			');
		end if;
	end;
	]]))

-- 创建 TChat 表，即聊天记录表。
-- id 行 id。
-- recv_user_id 聊天中的接收者。
-- send_user_id 聊天中的发送者，若此字段为空则表示发送者为系统。
-- content 消息的内容。
-- send_time 此消息的发送时刻。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_tables where table_name='TCHAT';
		if (a = 0) then
			execute immediate ('
				CREATE TABLE TChat(
					id number not null,
					recv_user_id varchar2(10) not null,
					send_user_id varchar2(10),
					content varchar2(4000) not null,
					send_time date not null)
			');
			execute immediate ('
				ALTER TABLE TChat ADD CONSTRAINT TChat_PK PRIMARY KEY (id)
			');
			execute immediate ('
				CREATE INDEX TChat_Index_1 ON TChat (
					recv_user_id, send_user_id, send_time)
			');
			execute immediate ('
				CREATE OR REPLACE TRIGGER TChat_Trigger_1
					BEFORE INSERT ON TChat
					FOR EACH ROW
				BEGIN
					SELECT TChatIdSeq.NEXTVAL INTO :NEW.id FROM DUAL;
				END;
			');
		end if;
	end;
	]]))

-- 创建用于 TUnreadChat 表的 id 字段的 Sequence。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_sequences where sequence_name='TUNREADCHATIDSEQ';
		if (a = 0) then
			execute immediate ('
				CREATE SEQUENCE TUnreadChatIdSeq
			');
		end if;
	end;
	]]))

-- 创建 TUnreadChat 表，即未读消息表。
-- recv_user_id 聊天中的接收者。
-- send_user_id 聊天中的发送者，若此字段为空则表示发送者为系统。
-- content 消息的内容。
-- send_time 此消息的发送时刻。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_tables where table_name='TUNREADCHAT';
		if (a = 0) then
			execute immediate ('
				CREATE TABLE TUnreadChat(
					id number not null,
					recv_user_id varchar2(10) not null,
					send_user_id varchar2(10),
					content varchar2(4000) not null,
					send_time date not null)
			');
			execute immediate ('
				ALTER TABLE TUnreadChat ADD CONSTRAINT TUnreadChat_PK PRIMARY KEY (id)
			');
			execute immediate ('
				CREATE INDEX TUnreadChat_Index_1 ON TUnreadChat (
					recv_user_id)
			');
			execute immediate ('
				CREATE OR REPLACE TRIGGER TUnreadChat_Trigger_1
					BEFORE INSERT ON TUnreadChat
					FOR EACH ROW
				BEGIN
					SELECT TUnreadChatIdSeq.NEXTVAL INTO :NEW.id FROM DUAL;
				END;
			');
		end if;
	end;
	]]))

-- 客服表。
-- user_id 用户号，来自海晟。
-- user_type 用户类别，取值如下：
--   消费者='X'
--   零售户='L'
--   客户经理='J'
--   客服='K'
--   工业公司='G'
--   商业公司='S'
--   未知='U'
-- user_name 用户名。
-- sex 性别。
assert(con:execute([[
	declare
		a number;
	begin
		select count(1) into a from user_tables where table_name='TKEFU';
		if (a = 0) then
			execute immediate ('
				CREATE TABLE TKeFu(
					user_id varchar2(10) not null ,
					user_type varchar2(1) not null,
					user_name varchar2(16) not null,
					sex varchar2(2) not null
				)
			');
			execute immediate ('
				ALTER TABLE TKeFu ADD CONSTRAINT TKeFu_PK PRIMARY KEY (user_id)
			');
		end if;
	end;
	]]))

-- 从数据库中加载用户信息。
-- 此函数供 c++ 调用。
function loadUserInfo(user_id)

	local sql =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		user_id .. "'"
	local cur = con:execute(sql)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end

	if cur ~= nil then
		local row = cur:fetch({}, "a")
		cur:close()
		if row then
			getUserInfoTable():add(
				user_id,
				UserInfo(row.user_name, row.user_type, row.sex))
		end
	end
end

-- 从数据库中查找用户信息，返回一个 table 对象，里面装了所有的 user_id。
-- 此函数供 c++ 调用。
function findUserByName(user_name)

	local sql =
		"SELECT user_id, user_type, user_name, sex FROM TUserInfo " ..
		"WHERE user_name = '" ..
		user_name .. "'"
	local cur = con:execute(sql)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end

	local tbl = {}
	if cur ~= nil then
		local row = cur:fetch({}, "a")
		while row do

			-- 若该用户信息已经在缓存中存在则此操作失败，但不影响。
			getUserInfoTable():add(
				row.user_id,
				UserInfo(row.user_name, row.user_type, row.sex))

			table.insert(tbl, row.user_id)

			row = cur:fetch(row, "a")
		end
		cur:close()
	end

	return tbl
end

-- 添加用户信息到数据库中，返回是否操作成功。
-- 此函数供 c++ 调用。
function addUserInfo(user_id, user_info)

	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"INSERT INTO TUserInfo(user_id, user_type, user_name, sex) VALUES('" ..
		user_id .. "', '" ..
		user_info:getUserType() .. "', '" ..
		user_info:getUserName() .. "', '" ..
		user_info:getSex() .. "')"
	return con:execute(sql) == 1
end

-- 加载好友列表。
-- 此函数供 c++ 调用。
function loadFriendList(user_id)

	local sql =
		"SELECT other_user_id FROM TFriend WHERE user_id='" .. user_id .. "'"
	local cur = con:execute(sql)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end

	local user_id_set = UserIdSet()
	if cur ~= nil then
		local row = cur:fetch({}, "a")
		while row do
			user_id_set:add(row.other_user_id)
			row = cur:fetch(row, "a")
		end
		cur:close()
	end

	getFriendListTable():add(user_id, user_id_set)
end

-- 添加好友到好友列表中，返回是否操作成功。
-- 此函数供 c++ 调用。
function addFriend(user_id, another_user_id)

	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"INSERT INTO TFriend(user_id, other_user_id) VALUES('" ..
		user_id .. "', '" ..
		another_user_id .. "')"
	return con:execute(sql) == 1
end

-- 删除好友，返回是否操作成功。
-- 此函数供 c++ 调用。
function removeFriend(user_id, another_user_id)

	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"DELETE FROM TFriend WHERE user_id='" .. user_id ..
		"' AND other_user_id='" .. another_user_id ..
		"'"
	return con:execute(sql) == 1
end

-- 加载屏蔽列表。
-- 此函数供 c++ 调用。
function loadShieldList(user_id)

	local sql =
		"SELECT other_user_id FROM TShield WHERE user_id='" .. user_id .. "'"
	local cur = con:execute(sql)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end

	local user_id_set = UserIdSet()
	if cur ~= nil then
		local row = cur:fetch({}, "a")
		while row do
			user_id_set:add(row.user_id)
			row = cur:fetch(row, "a")
		end
		cur:close()
	end

	getShieldListTable():add(user_id, user_id_set)
end

-- 添加用户到屏蔽列表中，返回是否操作成功。
-- 此函数供 c++ 调用。
function addShield(user_id, another_user_id)

	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"INSERT INTO TShield(user_id, other_user_id) VALUES('" ..
		user_id .. "', '" ..
		another_user_id .. "')"
	return con:execute(sql) == 1
end

-- 将指定的用户从屏蔽列表中删除，返回是否操作成功。
-- 此函数供 c++ 调用。
function removeShield(user_id, another_user_id)

	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"DELETE FROM TShield WHERE user_id='" .. user_id ..
		"' AND other_user_id='" .. another_user_id ..
		"'"
	return con:execute(sql) == 1
end

-- 加载最近联系人列表。
-- 此函数供 c++ 调用。
function loadRecentContactList(user_id)

	local sql =
		"SELECT DISTINCT send_user_id as user_id FROM TChat WHERE" ..
		" recv_user_id='" .. user_id ..
		"' AND send_user_id IS NOT NULL " ..
		"AND send_time>add_months(sysdate, -1) UNION " ..
		"SELECT DISTINCT recv_user_id as user_id FROM TChat WHERE" ..
		" send_user_id='" .. user_id ..
		"' AND send_time>add_months(sysdate, -1)"
	local cur = con:execute(sql)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end

	local user_id_set = UserIdSet()
	if cur ~= nil then
		local row = cur:fetch({}, "a")
		while row do
			user_id_set:add(row.user_id) -- 这里可以避免重复。
			row = cur:fetch(row, "a")
		end
		cur:close()
	end

	getRecentContactListTable():add(user_id, user_id_set)
end

-- 添加聊天记录，返回是否成功。
-- 此函数供 c++ 调用。
function addChat(recv_user_id, send_user_id, content)

	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		send_user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	-- 添加到聊天记录表中。
	local sql =
		"INSERT INTO TChat(" ..
			"recv_user_id, send_user_id, content, send_time" ..
		") VALUES('" ..
		recv_user_id .. "', '" ..
		send_user_id .. "', '" ..
		content .. "', " ..
		"sysdate)"
	local affect_counts = con:execute(sql)

	-- 添加到未读消息表中。
	local sql =
		"INSERT INTO TUnreadChat(" ..
			"recv_user_id, send_user_id, content, send_time" ..
		") VALUES('" ..
		recv_user_id .. "', '" ..
		send_user_id .. "', '" ..
		content .. "', " ..
		"sysdate)"
	con:execute(sql)

	return affect_counts == 1
end

-- 添加零售户的群发聊天记录，返回插入的条数。
-- 此函数供 c++ 调用。
function addGroupChatOfLingShouHu(send_user_id, content)

	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		send_user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"INSERT INTO TChat(" ..
			"recv_user_id, send_user_id, content, send_time" ..
		") SELECT a.other_user_id, a.user_id, '" .. content .. "', " ..
		"sysdate FROM TFriend a, TUserInfo b WHERE " ..
		"a.user_id='" .. send_user_id .. "' AND a.other_user_id=b.user_id AND " ..
		"b.user_type='X'"
	local affect_counts = con:execute(sql)

	local sql =
		"INSERT INTO TUnreadChat(" ..
			"recv_user_id, send_user_id, content, send_time" ..
		") SELECT a.other_user_id, a.user_id, '" .. content .. "', " ..
		"sysdate FROM TFriend a, TUserInfo b WHERE " ..
		"a.user_id='" .. send_user_id .. "' AND a.other_user_id=b.user_id AND " ..
		"b.user_type='X'"
	con:execute(sql)

	return affect_counts
end

-- 添加客户端经理的群发聊天记录，返回插入的条数。
-- 此函数供 c++ 调用。
function addGroupChatOfJingLi(send_user_id, content)
	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		send_user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"INSERT INTO TChat(" ..
			"recv_user_id, send_user_id, content, send_time" ..
		") SELECT a.other_user_id, a.user_id, '" .. content .. "', " ..
		"sysdate FROM TFriend a, TUserInfo b WHERE " ..
		"a.user_id='" .. send_user_id .. "' AND a.other_user_id=b.user_id AND " ..
		"b.user_type='L'"
	local affect_counts = con:execute(sql)

	local sql =
		"INSERT INTO TUnreadChat(" ..
			"recv_user_id, send_user_id, content, send_time" ..
		") SELECT a.other_user_id, a.user_id, '" .. content .. "', " ..
		"sysdate FROM TFriend a, TUserInfo b WHERE " ..
		"a.user_id='" .. send_user_id .. "' AND a.other_user_id=b.user_id AND " ..
		"b.user_type='L'"
	con:execute(sql)

	return affect_counts
end

-- 加载聊天记录列表，page 传 -1 时表示只加载页数。
-- 此函数供 c++ 调用。
function loadChatPage(user_id, another_user_id, page)

	local cpct = getChatPageCollectionTable()
	local cpc = cpct:getChatPageCollection(user_id, another_user_id)
	if cpc == nil then

		-- 首次创建页时需要从数据库中加载页数信息。
		local sql
		if user_id == "" then
			if another_user_id == "" then
				sql = "SELECT count(*) AS cnt FROM TChat WHERE recv_user_id IS NULL AND send_user_id IS NULL"
			else
				sql = "SELECT count(*) AS cnt FROM TChat WHERE (recv_user_id IS NULL AND send_user_id='" ..
					another_user_id ..
					"') OR (recv_user_id='" ..
					another_user_id ..
					"' AND send_user_id is null)"
			end
		else
			if another_user_id == "" then
				sql = "SELECT count(*) AS cnt FROM TChat WHERE (recv_user_id='" ..
					user_id ..
					"' AND send_user_id IS NULL) OR (recv_user_id IS NULL AND send_user_id='" ..
					user_id ..
					"')"
			else
				sql = "SELECT count(*) AS cnt FROM TChat WHERE (recv_user_id='" .. user_id ..
				"' AND send_user_id='" .. another_user_id ..
				"') OR (recv_user_id='" .. another_user_id ..
				"' AND send_user_id='" .. user_id ..
				"')"
			end
		end
		local cur = con:execute(sql)
	
		if cur == nil then		
			con = assert(env:connect(conn_str, "uss", "uss"))
			cur = con:execute(sql)
		end
		
		local num_records = 0
		if cur ~= nil then
			local row = cur:fetch({}, "a")
			if row then
				num_records = tonumber(row.cnt)
			end
		end

		cpc = ChatPageCollection(num_records, NUM_CHATS_PER_PAGE)
		cpct:add(user_id, another_user_id, cpc)
	end
	if page == -1 then
		return
	end

	local cp = cpc:getChatPage(page)
	if cp == nil then
		cp = ChatPage(NUM_CHATS_PER_PAGE)
		cpc:add(page, cp)
	end

	local from_idx = page * NUM_CHATS_PER_PAGE
	local sql
	if user_id == "" then
		if another_user_id == "" then
			sql = [[
				SELECT recv_user_id, send_user_id, content,
					to_char(send_time, 'YYYY-MM-DD HH24-MI-SS') send_time FROM (
					SELECT a.*, rownum rn FROM (
						SELECT
							recv_user_id, send_user_id, content, send_time
						FROM TChat
						WHERE recv_user_id IS NULL AND send_user_id IS NULL ORDER BY send_time ASC
					) a WHERE rownum<=]] .. from_idx + NUM_CHATS_PER_PAGE ..
				") WHERE rn>" .. from_idx
		else
			sql = [[
				SELECT recv_user_id, send_user_id, content,
					to_char(send_time, 'YYYY-MM-DD HH24-MI-SS') send_time FROM (
					SELECT a.*, rownum rn FROM (
						SELECT
							recv_user_id, send_user_id, content, send_time
						FROM TChat
						WHERE (recv_user_id IS NULL AND send_user_id=']] .. another_user_id ..
							"') OR (recv_user_id='" .. another_user_id ..
							[[' AND send_user_id IS NULL) ORDER BY send_time ASC
					) a WHERE rownum<=]] .. from_idx + NUM_CHATS_PER_PAGE ..
				") WHERE rn>" .. from_idx
		end
	else
		if another_user_id == "" then
			sql = [[
				SELECT recv_user_id, send_user_id, content,
					to_char(send_time, 'YYYY-MM-DD HH24-MI-SS') send_time FROM (
					SELECT a.*, rownum rn FROM (
						SELECT
							recv_user_id, send_user_id, content, send_time
						FROM TChat
						WHERE (recv_user_id=']] .. user_id ..
							"' AND send_user_id IS NULL) OR (recv_user_id IS NULL AND send_user_id='" .. user_id ..
							[[') ORDER BY send_time ASC
					) a WHERE rownum<=]] .. from_idx + NUM_CHATS_PER_PAGE ..
				") WHERE rn>" .. from_idx
		else
			sql = [[
				SELECT recv_user_id, send_user_id, content,
					to_char(send_time, 'YYYY-MM-DD HH24-MI-SS') send_time FROM (
					SELECT a.*, rownum rn FROM (
						SELECT
							recv_user_id, send_user_id, content, send_time
						FROM TChat
						WHERE (recv_user_id=']] .. user_id ..
							"' AND send_user_id='" .. another_user_id ..
							"') OR (recv_user_id='" .. another_user_id ..
							"' AND send_user_id='" .. user_id ..
							[[') ORDER BY send_time ASC
					) a WHERE rownum<=]] .. from_idx + NUM_CHATS_PER_PAGE ..
				") WHERE rn>" .. from_idx
		end
	end
	local cur = con:execute(sql)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end

	if cur ~= nil then
		local row = cur:fetch({}, "a")
		while row do
			local send_user_id
			if row.send_user_id == nil then
				send_user_id = ""
			else
				send_user_id = row.send_user_id
			end
			local chat = Chat(
				send_user_id,
				row.recv_user_id,
				row.content,
				row.send_time)
			cp:add(chat)
			row = cur:fetch(row, "a")
		end
		cur:close()
	end
end

-- 加载所有未读消息，返回消息页（即将所有消息放入一个消息页中返回）。
-- 此函数供 c++ 调用。
function loadUnreadMsgs(user_id)

	-- 查询所有未读消息。
	local sql = [[
		SELECT recv_user_id, send_user_id, content,
			to_char(send_time, 'YYYY-MM-DD HH24-MI-SS') send_time
		FROM TUnreadChat WHERE recv_user_id=']] ..
		user_id .. "'"
	local cur = con:execute(sql)

	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end
	-- 将查询到的未读消息装入消息页中。
	local cp = nil
	if cur ~= nil then
		cp = ChatPage(20) -- 20 只是说这页预留 20 条记录，实际可以超过此值。

		local row = cur:fetch({}, "a")
		while row do
			local send_user_id
			if row.send_user_id == nil then
				send_user_id = ""
			else
				send_user_id = row.send_user_id
			end
			local chat = Chat(
				send_user_id,
				row.recv_user_id,
				row.content,
				row.send_time)
			cp:add(chat)
			row = cur:fetch(row, "a")
		end
		cur:close()
	end

	-- 删除所有未读信息。
	if cp ~= nil and cp:getNumChats() > 0 then
		sql = "DELETE FROM TUnreadChat WHERE recv_user_id='" .. user_id .. "'"
		con:execute(sql)
	end

	if cp == nil then
		print("cp is nil")
	end

	-- 返回消息页。
	return cp
end

-- 删除所有未读消息。
-- 此函数供 c++ 调用。
function deleteUnreadMsgs(user_id)
	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		send_user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql = "DELETE FROM TUnreadChat WHERE user_id='" .. user_id .. "'"
	con:execute(sql)
end

-- 添加客服。
-- 此函数供 c++ 调用。
function addKeFu(user_id, user_type, user_name, sex)
	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		send_user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql =
		"INSERT INTO TKeFu(user_id, user_type, user_name, sex) VALUES('" ..
		user_id .. "', '" .. user_type .. "', '" .. user_name .. "', '" ..
		sex .. "')"
	local affect_counts = con:execute(sql)

	local sql =
		"INSERT INTO TUserInfo(user_id, user_type, user_name, sex) VALUES('" ..
		user_id .. "', '" ..
		user_type .. "', '" ..
		user_name .. "', '" ..
		sex .. "')"
	con:execute(sql)

	return affect_counts == 1
end

-- 删除客服。
-- 此函数供 c++ 调用。
function deleteKeFu(user_id)
	local sqltemp =
		"SELECT user_type, user_name, sex FROM TUserInfo WHERE user_id='" ..
		send_user_id .. "'"
	local cur = con:execute(sqltemp)
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
	end

	local sql = "DELETE FROM TKeFu WHERE user_id='" .. user_id .. "'"
	return con:execute(sql) == 1
end

-- 加载客服列表，返回客服用户表。
function loadKeFuList()
	local sql = "SELECT user_id, user_type, user_name, sex FROM TKeFu"
	local cur = con:execute(sql)		
	
	if cur == nil then		
		con = assert(env:connect(conn_str, "uss", "uss"))
		cur = con:execute(sql)
	end
	
	if cur ~= nil then
		local tbl = getKeFuUserInfoTable()
		local row = cur:fetch({}, "a")
		while row do
			tbl:add(
				row.user_id,
				UserInfo(row.user_name, row.user_type, row.sex))
			row = cur:fetch(row, "a")
		end
		cur:close()
	end
end
