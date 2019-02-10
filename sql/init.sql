

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
  `sshport`       int(5)  not null default 22  comment 'ssh通讯端口',
  `nodedesc`      varchar(30) comment '描述',
  `nodetype`      int(1)  not null default 1 comment '节点类型0:目录 1:机器'
)
comment = '主机树表';

insert into t_host (nodepid,nodedesc,nodetype) values (0,'集群',0);

drop table if exists t_host_config_rule;
create table if not exists t_host_config_rule (
  `ruleid`    int(10) not null auto_increment comment 'id' primary key,
  `matchstr`     varchar(512) not null comment 'ip匹配表达式',
  `timeout`   int(5)  not null default 20000  comment '连接超时时间',
  `loginname`  varchar(64)  not null comment '登录用户名',
  `loginpwd`  varchar(512)  not null comment '登录密码'
)
comment = '主机配置表';

drop table if exists t_ssh_log;
create table if not exists t_ssh_log (
  `sshtime`   datetime comment '会话建立时间',
	`fieldyear` varchar(4)  not null comment '年',
	`fieldmonth` varchar(2)  not null comment '月',
	`ddate`			varchar(10)		not null comment '年月日',
	`dtime`			varchar(8)		not null comment '时分秒',
	`username`  varchar(64)  not null comment '登录用户名',
	`hostinfo`  varchar(512) comment '主机信息',
	index `idx_t_ssh_log_fieldyear`(`fieldyear`),
	index `idx_t_ssh_log_username`(`username`)
)
comment = 'ssh登录日志表';