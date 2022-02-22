-- 创建博客表
DROP TABLE IF EXISTS `bl_blog`;

CREATE TABLE `bl_blog` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `type_id` varchar(128) NOT NULL COMMENT '博客分类ID',
  `title` varchar(32) DEFAULT NULL COMMENT '博客标题',
  `img_url` varchar(255) DEFAULT NULL COMMENT '博客略缩图URL地址',
  `content` mediumtext COMMENT '博客内容',
  `desc` varchar(255) DEFAULT NULL COMMENT '博客描述',
  `origin` int(1) DEFAULT '1' COMMENT '原创标识，1为原创',
  `commend` int(1) DEFAULT '0' COMMENT '推荐标识，1为推荐',
  `release` int(1) DEFAULT '1' COMMENT '发布标识，1为发布',
  `reward` int(1) DEFAULT '1' COMMENT '赞赏标识，1为赞赏',
  `view_count` int(11) DEFAULT NULL COMMENT '博客浏览次数',
  `create_time` bigint(20) DEFAULT NULL COMMENT '博客发布时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '博客修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客表';



-- 创建字段表
--   id：唯一标识
--   field：字段英文名
--   name：字段中文名
--   value：字段值
--   desc：字段详细描述
DROP TABLE IF EXISTS `bl_field`;

CREATE TABLE `bl_field` (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `field` varchar(16) DEFAULT NULL COMMENT '字段名称',
  `name` varchar(16) DEFAULT NULL COMMENT '作用名称',
  `value` varchar(255) DEFAULT NULL COMMENT '字段值',
  `desc` varchar(32) DEFAULT NULL COMMENT '作用描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字段表';

INSERT INTO `bl_field`(`id`,`field`,`name`,`value`,`desc`) VALUES
(NULL,'account','管理员账号','admin',NULL),
(NULL,'password','管理员密码','F59BD65F7EDAFB087A81D4DCA06C4910',NULL),
(NULL,'title','网站标题','个人博客网站',NULL),
(NULL,'username','站长昵称','张涵哲',NULL),
(NULL,'wechatNum','微信账号','i-223344',NULL),
(NULL,'qqNum','QQ账号','1452079621',NULL),
(NULL,'wechatQr','微信二维码','static/img/wechatQr.jpg',NULL),
(NULL,'qqQr','QQ二维码','static/img/qqQr.jpg',NULL),
(NULL,'wechatPay','微信收款码','static/img/wechatPay.jpg',NULL),
(NULL,'aliPay','支付宝收款码','static/img/aliPay.jpg',NULL),
(NULL,'adminImg','管理员头像','static/img/admin.jpg',NULL),
(NULL,'guestImg','游客头像','static/img/guest.jpg',NULL),
(NULL,'aboutImg','关于我大图','static/img/about.jpg',NULL),
(NULL,'aboutInfo','关于我描述','欢迎来到我的博客，小弟Java开发小学生一枚，我会在这里分享一些我学习到的知识和遇到的问题以及对应的解决方案，感谢您的浏览！',''),
(NULL,'aboutTag','关于我标签','死肥宅-小黑胖子-爱看动漫-社交恐惧-习惯孤独',NULL),
(NULL,'indexInfo','首页关于我','在下擅长 Ai、Br、Ae、Pr、Id、Ps 等软件的安装与卸载，精通CSS、JavaScript、PHP、ASP、C、C++、C#、Java、Ruby、Perl、Lisp、Python、MySQL、Oracle、Pascal 等单词的拼写，熟悉 Windows、Linux、OS X、Android、iOS、WP8 等系统的开关机。',NULL),
(NULL,'notice','网站公告','新域名 http://blog.hanzhe.site 已经启动！',NULL),
(NULL,'email','邮箱地址','zhang_hanzhe@qq.com',NULL),
(NULL,'pageSize','分页显示数量',5,NULL);



-- 创建友链表
DROP TABLE IF EXISTS `bl_link`;

CREATE TABLE `bl_link` (
  `id` int(3) NOT NULL COMMENT '主键ID',
  `name` varchar(16) DEFAULT NULL COMMENT '网站名称',
  `url` varchar(255) DEFAULT NULL COMMENT '网站地址URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='友链表';


INSERT INTO `bl_link`(`id`,`name`,`url`) VALUES 
(1,'联系站长申请友链','#'),
(2,'联系站长申请友链','#'),
(3,'联系站长申请友链','#'),
(4,'联系站长申请友链','#');



-- 创建留言表
DROP TABLE IF EXISTS `bl_message`;

CREATE TABLE `bl_message` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `parent` varchar(128) DEFAULT NULL COMMENT '父级ID',
  `name` varchar(16) DEFAULT NULL COMMENT '用户名称',
  `email` varchar(32) DEFAULT NULL COMMENT '用户邮箱',
  `content` varchar(512) DEFAULT NULL COMMENT '留言内容',
  `create_time` bigint(20) DEFAULT NULL COMMENT '留言时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='留言表';



-- 创建分类表
DROP TABLE IF EXISTS `bl_type`;

CREATE TABLE `bl_type` (
  `id` varchar(128) NOT NULL COMMENT '主键ID',
  `name` varchar(16) DEFAULT NULL COMMENT '分类名称',
  `create_time` bigint(20) DEFAULT NULL COMMENT '分类创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '分类修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类表';