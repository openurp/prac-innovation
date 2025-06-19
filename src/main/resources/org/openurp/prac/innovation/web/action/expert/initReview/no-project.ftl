[@b.head/]
<script>
   beangle.load(["adminlte"],function(){});
</script>
<div class="container-fluid">
<nav class="navbar navbar-expand-lg navbar-light bg-light" style="margin-bottom: 0px;">
  <a class="navbar-brand" href="#">大学生创新项目立项专家评审系统</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse">
    <ul class="nav navbar-nav navbar-right mr-auto">
    </ul>
    <ul class="nav navbar-nav">
      <li>
       <i class="fa-solid fa-user"></i>${expert.name}(${expert.code})
      </li>
      <li>
       <a href="${b.url('!logout')}" target="_top"><i class="fa-solid fa-right-from-bracket"></i>退出&nbsp;&nbsp;</span></a>
      </li>
    </ul>
  </div>
</nav>
  <div class="m-auto alert alert-warning">没有分配任何项目,请联系管理人员。</div>
[@b.foot/]
