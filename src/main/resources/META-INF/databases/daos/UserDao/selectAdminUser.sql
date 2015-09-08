SELECT
    user.userid, user.username
FROM
    user
INNER JOIN
    admin
ON
    admin.userid = user.userid
WHERE
    user.userid = /* userid */'userid'
