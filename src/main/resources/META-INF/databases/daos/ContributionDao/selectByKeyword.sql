SELECT
    /*%expand*/*
FROM
    contribution
WHERE
    title LIKE /* keyword */'hoge'
    OR
    content LIKE /* keyword */'hoge'
ORDER BY
    id
DESC