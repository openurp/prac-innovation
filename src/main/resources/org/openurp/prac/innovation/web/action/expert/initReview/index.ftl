[@b.head]
  <style>
.login-page {
    height: auto;
    background: #d2d6de;
}
.login-box  {
    width: 400px;
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
<div class="container">
<div class="login-box">
  <div class="login-logo">
    <b>大学生创新项目专家立项评审系统</b>
  </div>
  <div class="login-box-body">
    <p class="login-box-msg">登录系统</p>
    [@b.messages flash="3"/]
    <form action="${b.base}/expert/init-review/login" method="post">
      <div class="input-group">
        <div class="input-group-prepend"><div class="input-group-text" style="width: 40px;"><i class="fa-solid fa-user"></i></div></div>
        <input type="text" name="code" class="form-control" placeholder="用户名">
      </div>
      <div class="input-group">
        <div class="input-group-prepend"><div class="input-group-text" style="width: 40px;"><i class="fa-solid fa-key"></i></div></div>
        <input type="password" name="password" class="form-control" placeholder="密码" style="height:calc(2.25rem + 2px);width: 1%;padding:.375rem .75rem">
      </div>
      <div class="row" style="text-align: center;">
        <div class="col-md-12">
          <button type="submit" class="btn btn-primary btn-sm">登录</button>
        </div>
      </div>
    </form>
  </div>
</div>
</div>
[@b.foot/]
