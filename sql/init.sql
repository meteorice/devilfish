CREATE TABLE t_user (
  `userid`        int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username`      varchar(50) COMMENT '用户名',
  `password`      varchar(512) COMMENT '密码',
  `status`        int(1) DEFAULT 1 COMMENT '1正常',
  `lastlogintime` datetime COMMENT '最后一次登入时间',
  `createtime`    datetime COMMENT '创建时间',
  INDEX `idx_user_id`(`userid`)
)
  COMMENT = '用户表';