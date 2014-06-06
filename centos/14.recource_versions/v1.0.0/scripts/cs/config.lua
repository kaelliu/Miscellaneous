-- 某人是否可以添加另一个人为好友，其中 user_type 的取值如下：
-- 消费者='X'
-- 零售户='L'
-- 客户经理='J'
-- 客服='K'
-- 工业公司='G'
-- 商业公司='S'
-- 未知='U'
function canAddFriend(user_type, target_user_type)

--~ 	if (user_type == 'L') then
--~ 		if (target_user_type == 'X' or
--~ 			target_user_type == 'K' or
--~ 			target_user_type == 'J') then
--~ 			return true
--~ 		end
--~ 	elseif (user_type == 'X') then
--~ 		if (target_user_type == 'L' or
--~ 			target_user_type == 'K' or
--~ 			target_user_type == 'X') then
--~ 			return true
--~ 		end
--~ 	elseif (user_type == 'G') then
--~ 		if (target_user_type == 'K' or
--~ 			target_user_type == 'G') then
--~ 			return true
--~ 		end
--~ 	elseif (user_type == 'J') then
--~ 		if (target_user_type == 'L' or
--~ 			target_user_type == 'K' or
--~ 			target_user_type == 'J') then
--~ 			return true
--~ 		end
--~ 	end

--~ 	return false

	return true
end

-- 某人是否可以添加另一个人到黑名单中，其中 user_type 的取值如下：
-- 消费者='X'
-- 零售户='L'
-- 客户经理='J'
-- 客服='K'
-- 工业公司='G'
-- 商业公司='S'
-- 未知='U'
function canAddShield(user_type, target_user_type)

--~ 	if (target_user_type == 'K') then
--~ 		return false
--~ 	end

--~ 	if (user_type == 'L' and target_user_type == 'J') then
--~ 		return false
--~ 	end

--~ 	if (user_type == 'J' and target_user_type == 'L') then
--~ 		return false
--~ 	end

	return true
end
