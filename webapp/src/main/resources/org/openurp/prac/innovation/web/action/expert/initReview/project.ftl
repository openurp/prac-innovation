[@b.head/]
<script>
   beangle.load(["adminlte"],function(){});
</script>
<nav class="navbar navbar-default" style="margin-bottom: 0px;">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" id="toggleButton" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <img src="${detail.review.project.department.school.logoUrl}" style="width:50px;height:50px;float: left !important;">
      <a class="navbar-brand" href="#">大学生创新项目立项专家评审系统</a>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav navbar-right">
        <li>
         <a href="#"><span class="glyphicon glyphicon-user" aria-hidden="true">${expert.name}(${expert.code})</span></a>
        </li>
        <li>
         <a href="${b.url('!logout')}" target="_top"><span class="glyphicon glyphicon-log-out" aria-hidden="true">退出&nbsp;&nbsp;</span></a>
        </li>
      </ul>
    </div>
  </div>
</nav>
[@b.messages  slash="3"/]
<section class="content">
    <div class="row">
      <div class="col-md-3">
        <div class="box box-solid">
          <div class="box-header with-border">
            <h3 class="box-title">项目列表<span class="badge">${details?size}</span></h3>
            <div class="box-tools">
              <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
              </button>
            </div>
          </div>
          <div class="box-body no-padding">
            <ul class="nav nav-pills nav-stacked">
              [#list details as r]
               <li [#if detail.id=r.id]class="active"[/#if]>
                 [@b.a href="!project?detail.id="+r.id]
                 <i class="fa fa-file-text-o"></i>${r.review.project.title} [#if r.score??]<span class="label label-warning pull-right">${r.score} [#if r.passed??]${r.passed?string("同意","不同意")}[/#if]</span>[/#if]
                 [/@]
               </li>
              [/#list]
            </ul>
          </div>
        </div>
    </div>

    <div class="col-md-9">
      <div class="box box-primary">
            <div class="box-header with-border">
              <h3 class="box-title">项目详情</h3>
              [#assign project=detail.review.project/]
              <div class="box-tools pull-right">
                <a href="#" class="btn btn-box-tool" data-toggle="tooltip" title="" data-original-title="Previous"><i class="fa fa-chevron-left"></i></a>
                <a href="#" class="btn btn-box-tool" data-toggle="tooltip" title="" data-original-title="Next"><i class="fa fa-chevron-right"></i></a>
              </div>
            </div>
            <div class="box-body no-padding">
              <div class="mailbox-read-info">
                <h3>${project.title}</h3>
                <h5>负责人: ${project.manager.std.user.name} 指导老师: [#list (project.instructors)! as i]${i.user.name}[#if i_has_next],[/#if][/#list]
                  <span class="mailbox-read-time pull-right">${(project.discipline.name)!} ${project.code!}</span>
                </h5>
              </div>
              <div class="mailbox-read-message">
                <h4>简介</h4>
                <p>${(project.intro.summary)!}</p>
                <h4>创新点和难点</h4>
                <p>${(project.intro.innovation)!}</p>
                <h4>预期成果</h4>
                <p>${(project.intro.product)!}</p>
                <h4>立项材料</h4>
                <p>
                [#list project.materials as m]
                   [#if m.stageType.id ==InitStageId]
                    [@b.a href="!attachment?id="+detail.id class="mailbox-attachment-name"]<i class="fa fa-paperclip"></i>${m.fileName} <i class="fa fa-cloud-download"></i>[/@]
                   [/#if]
                [/#list]
                </p>
              </div>
            </div>
           </div>

           <div class="box box-primary">
             <form class="form-horizontal" name="review_form" action="${b.url("!save?id="+detail.id)}" method="post" onsubmit="return check_detail(this);">
               <div class="box-body">
                 <div class="form-group">
                   <label for="detail_score" class="col-sm-2 control-label">评审分数: </label>
                   <div class="col-sm-10">
                     <input type="number" name="detail.score" value="${detail.score!}" min="1" max="100" class="form-control" id="detail_score" placeholder="1~100分">
                   </div>
                 </div>
                 <div class="form-group">
                   <label for="detail_passed" class="col-sm-2 control-label">是否推荐立项: </label>
                   <div class="col-sm-10">
                     <input type="radio" name="detail.passed" [#if detail.passed?? && detail.passed] checked="checked"[/#if] value="1" id="detail_pass_1"> <label for="detail_pass_1">同意</label>
                     <input type="radio" name="detail.passed" [#if detail.passed?? && !detail.passed] checked="checked"[/#if] value="0" id="detail_pass_0"> <label for="detail_pass_0">不同意</label>
                   </div>
                 </div>
                 <div class="form-group">
                   <label for="review_comments" class="col-sm-2 control-label">评审意见: </label>
                   <div class="col-sm-10">
                     <textarea name="detail.comments" id="review_comments" class="form-control" rows="4" placeholder="500字以内的评审意见">${detail.comments!}</textarea>
                   </div>
                 </div>
               </div>
               <div class="box-footer">
                 <button type="submit" class="btn btn-info pull-right">提交</button>
               </div>
             </form>
           </div>
    </div>
  </div>
</section>
<script>
   function check_detail(form){
     if(!form["detail.passed"].value){
       alert("请选择是否推荐该项目立项");
       return false;
     }
     if(!form['detail.score'].value){
       alert("请填写评分");
       return false;
     }
     if(!form['detail.comments'].value){
       alert("请填写评审意见");
       return false;
     }
     return true;
   }
</script>
[@b.foot/]
