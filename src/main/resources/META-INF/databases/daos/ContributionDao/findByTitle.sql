SELECT
    /*%expand*/*
FROM
    contribution
WHERE
    title
LIKE
    /* title */'hoge'
ORDER BY
    id
DESC