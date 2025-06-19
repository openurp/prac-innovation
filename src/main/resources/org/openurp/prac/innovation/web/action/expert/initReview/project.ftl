[@b.head/]
<script>
   beangle.load(["adminlte"],function(){});
</script>
<div class="container-fluid">
<nav class="navbar navbar-expand-lg navbar-light bg-light" style="margin-bottom: 0px;">
  <a class="navbar-brand" href="#"><img src="${(detail.review.project.department.school.logoUrl)!}" style="height:30px;float: left !important;">大学生创新项目立项专家评审系统</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse">
    <div class="mr-auto text-muted">
      ${detail.review.project.batch.name}
    </div>
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

[@b.messages  slash="3"/]
<section class="content">
    <div class="row">
      <div class="col-md-3">
        <div class="card card-outline card-primary">
          <div class="card-header with-border">
            <h3 class="card-title">项目列表<span class="badge">${details?size}</span></h3>
            <div class="card-tools">
              <button type="button" class="btn btn-tool" data-widget="collapse"><i class="fa fa-minus"></i>
              </button>
            </div>
          </div>
          <div class="card-body" style="overflow-y: scroll;max-height:720px;">
            <ul class="nav nav-pills flex-column">
              [#list details as r]
              <li class="nav-item">
               [#assign item_link_class][#if detail.id=r.id]nav-link active[#else]nav-link[/#if][/#assign]
               [@b.a href="!project?detail.id="+r.id class=item_link_class]
               <i class="fa-regular fa-file"></i> ${r.review.project.title} [#if r.score??]<span class="label label-warning pull-right">${r.score} [#if r.passed??]${r.passed?string("同意","不同意")}[/#if]</span>[/#if]
               [/@]
              </li>
              [/#list]
            </ul>
          </div>
        </div>
    </div>

    <div class="col-md-9">
      <div class="card card-outline card-primary">
            <div class="card-header">
              [#assign project=detail.review.project/]
              <h5 class="card-title">${project.title}</h5>
              <div class="card-tools pull-right">
                <a href="#" class="btn btn-tool" data-toggle="tooltip" title="" data-original-title="Previous"><i class="fa fa-chevron-left"></i></a>
                <a href="#" class="btn btn-tool" data-toggle="tooltip" title="" data-original-title="Next"><i class="fa fa-chevron-right"></i></a>
              </div>
            </div>
            <div class="card-body" style="padding-top:0px;">
              <div class="mailbox-read-info">
                <h6>负责人: ${project.manager.std.name} 指导老师: [#list (project.instructors)! as i]${i.name}[#if i_has_next],[/#if][/#list]
                  <span class="mailbox-read-time pull-right">${(project.discipline.name)!} ${project.code!}</span>
                </h6>
              </div>
              <div class="mailbox-read-message">
                <h6>简介</h6>
                <p>${(project.intro.summaries)!}</p>
                <h6>创新点和难点</h6>
                <p>${(project.intro.innovations)!}</p>
                <h6>预期成果</h6>
                <p>${(project.intro.products)!}</p>
                <h6>立项材料</h6>
                <p>
                [#list project.materials as m]
                   [#if m.stageType.id ==InitStageId]
                    [@b.a href="!attachment?id="+detail.id class="mailbox-attachment-name" target="_blank"]<i class="fa fa-paperclip"></i>${m.fileName} <i class="fa fa-cloud-download"></i>[/@]
                   [/#if]
                [/#list]
                </p>
              </div>
            </div>
           </div>

           <div class="card card-outline">
             <form class="form-horizontal" name="review_form" action="${b.url("!save?id="+detail.id)}" method="post" onsubmit="return check_detail(this);">
               <div class="card-body">
                 <div class="form-group" style="display:none;">
                   <label for="detail_score" class="col-sm-2 control-label">评审分数: </label>
                   <div class="col-sm-10">
                     <input type="number" name="detail.score" value="${detail.score!100}" min="1" max="100" class="form-control" id="detail_score" placeholder="1~100分">
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
               <div class="card-footer">
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
</div>
[@b.foot/]
