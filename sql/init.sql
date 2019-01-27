

drop table if exists t_user;
create table if not exists t_user (
  `userid`        int(11) not null auto_increment comment '用户id',
  `username`      varchar(50) comment '用户名',
  `password`      varchar(512) comment '密码',
  `status`        int(1) default 1 comment '1正常',
  `lastlogintime` datetime comment '最后一次登入时间',
  `createtime`    datetime comment '创建时间',
  index `idx_user_id`(`userid`)
)
comment = '用户表';

drop table if exists t_host;
create table if not exists t_host (
  `nodeid`        int(10) not null auto_increment comment 'id' primary key,
  `nodepid`       int(10) not null comment 'pid',
  `hostip`        varchar(30) comment '主机ip',
  `nodedesc`      varchar(30) comment '描述',
  `nodetype`      int(1)  not null default 1 comment '节点类型0:目录 1:机器'
)
comment = '主机树表';

insert into t_host (nodepid,nodedesc,nodetype) values (0,'集群',0);

drop table if exists t_password_rule;
create table if not exists t_password_rule (
  `ruleid`    int(10) not null auto_increment comment 'id' primary key,
  `match`     varchar(512) not null comment '正则表达式',
  `loginname`  varchar(64)  not null comment '登录用户名',
  `loginpwd`  varchar(512)  not null comment '登录密码',
)
comment = '密码规则表';