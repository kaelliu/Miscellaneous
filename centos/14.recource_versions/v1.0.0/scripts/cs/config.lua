-- ĳ���Ƿ���������һ����Ϊ���ѣ����� user_type ��ȡֵ���£�
-- ������='X'
-- ���ۻ�='L'
-- �ͻ�����='J'
-- �ͷ�='K'
-- ��ҵ��˾='G'
-- ��ҵ��˾='S'
-- δ֪='U'
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

-- ĳ���Ƿ���������һ���˵��������У����� user_type ��ȡֵ���£�
-- ������='X'
-- ���ۻ�='L'
-- �ͻ�����='J'
-- �ͷ�='K'
-- ��ҵ��˾='G'
-- ��ҵ��˾='S'
-- δ֪='U'
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
