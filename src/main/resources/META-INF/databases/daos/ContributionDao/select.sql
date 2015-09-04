SELECT
    /*%expand*/*
FROM
    contribution
WHERE
    userid = /* userid */'hoge'
ORDER BY
    id
DESC
