[@b.head]
  <style>
.login-page {
    height: auto;
    background: #d2d6de;
}
.login-box  {
    width: 360px;
    margin: 7% auto;
}
.login-logo  {
    font-size: 25px;
    text-align: center;
    margin-bottom: 25px;
    font-weight: 300;
}
.login-box-body  {
    background: #fff;
    padding: 20px;
    border-top: 0;
    color: #666;
}
.login-box-msg  {
    margin: 0;
    text-align: center;
    padding: 0 20px 20px 20px;
}
  </style>
[/@]
<body class="hold-transition login-page">
<div class="login-box">
  <div class="login-logo">
    <b>大学生创新项目专家评审系统</b>
  </div>
  <div class="login-box-body">
    <p class="login-box-msg">登录系统</p>
    [@b.messages flash="3"/]
    <form action="${base}/expert/review/login" method="post">
      <div class="form-group has-feedback">
        <input type="text" name="code" class="form-control" placeholder="用户名">
        <span class="glyphicon glyphicon-user form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <input type="password" name="password" class="form-control" placeholder="密码">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      <div class="row">
        <div class="col-xs-8">
          <div class="checkbox icheck">
          </div>
        </div>
        <div class="col-xs-4">
          <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
        </div>
      </div>
    </form>
  </div>
</div>
[@b.foot/]
