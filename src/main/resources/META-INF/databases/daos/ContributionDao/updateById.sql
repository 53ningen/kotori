UPDATE
  contribution
SET
  content = /* payload.content */'hoge'
  /*%if payload.username != null */
  ,username = /* payload.username */'hoge'
  /*%end*/
  /*%if payload.title != null */
  ,title = /* payload.title */'hoge'
  /*%end*/
WHERE
  id = /* id */0