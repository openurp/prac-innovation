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
      <img src="${review.project.department.school.logoUrl}" style="width:50px;height:50px;float: left !important;">
      <a class="navbar-brand" href="#">大学生创新项目专家评审系统</a>
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
        <div class="card card-primary card-outline">
          <div class="card-header with-border">
            <h3 class="card-title">项目列表<span class="badge">${details?size}</span></h3>
            <div class="card-tools">
              <button type="button" class="btn btn-card-tool" data-widget="collapse"><i class="fa fa-minus"></i>
              </button>
            </div>
          </div>
          <div class="card-body">
            <ul class="nav nav-pills flex-column">
              [#list details as r]
               <li class="nav-item">
                 [#assign projectStyle="nav-link"/]
                 [#if reviewDetail.id=r.id][#assign projectStyle="nav-link active"/][/#if]
                 [@b.a href="!project?review.id="+r.id class=projectStyle]
                 <i class="fa fa-file-text-o"></i>${r.review.project.title} [#if r.score??]<span class="label label-warning pull-right">${r.level.name} ${r.score}</span>[/#if]
                 [/@]
               </li>
              [/#list]
            </ul>
          </div>
        </div>

        <div class="card card-primary card-outline">
          <div class="card-header">
            <h3 class="card-title">推荐级别统计</h3>

            <div class="card-tools">
              <button type="button" class="btn btn-card-tool" data-widget="collapse"><i class="fa fa-minus"></i>
              </button>
            </div>
          </div>
          <div class="card-body">
            <ul class="nav nav-pills flex-column">
             [#assign levelCounts={}/]
             [#list details as r]
               [#if r.level??]
                 [#assign levelCounts=levelCounts + {r.level.id?string:(levelCounts[r.level.id?string]!0)+1}/]
               [/#if]
             [/#list]
              [#assign colors=['text-red','text-yellow','text-light-blue']/]
              [#list levels as l]
              <li class="nav-item">
                <a  class="nav-link" href="#"><i class="fa fa-circle-o ${colors[l_index]}"></i>${l.name}
                <span class="label label-primary pull-right">${levelCounts[l.id?string]!0}</span>
                </a>
              </li>
              [/#list]
            </ul>
          </div>
        </div>
      </div>

    <div class="col-md-9">
      <div class="card card-primary card-outline">
            <div class="card-header">
              <h3 class="card-title">项目详情</h3>
              [#assign project=review.project/]
              <div class="card-tools pull-right">
                <a href="#" class="btn btn-card-tool" data-toggle="tooltip" title="" data-original-title="Previous"><i class="fa fa-chevron-left"></i></a>
                <a href="#" class="btn btn-card-tool" data-toggle="tooltip" title="" data-original-title="Next"><i class="fa fa-chevron-right"></i></a>
              </div>
            </div>
            <div class="card-body">
              <div class="mailbox-read-info">
                <h3>${review.project.title}</h3>
                <h5>负责人: ${review.project.manager.std.user.name} 指导老师: [#list (project.instructors)! as i]${i.user.name}[#if i_has_next],[/#if][/#list]
                  <span class="mailbox-read-time pull-right">${(project.discipline.name)!} ${review.project.code!}</span>
                </h5>
              </div>
              <div class="mailbox-read-message">
                <h4>简介</h4>
                <p>${(project.intro.summary)!}</p>
                <h4>创新点和难点</h4>
                <p>${(project.intro.innovation)!}</p>
                <h4>预期成果</h4>
                <p>${(project.intro.product)!}</p>
                <h4>结项材料</h4>
                <p>
                [#list project.materials as m]
                   [#if m.stageType.id ==ClosureStageId]
                    [@b.a href="!attachment?id="+reviewDetail.id class="mailbox-attachment-name"]<i class="fa fa-paperclip"></i>${m.fileName} <i class="fa fa-cloud-download"></i>[/@]
                   [/#if]
                [/#list]
                </p>
              </div>
            </div>
           </div>

           <div class="card card-primary card-outline">
             <form class="form-horizontal" name="review_form" action="${b.url("!save?id="+reviewDetail.id)}" method="post" onsubmit="return check_review(this);">
               <div class="card-body">
                 <div class="form-group">
                   <label for="review_score" class="col-sm-2 control-label">评审分数: </label>
                   <div class="col-sm-10">
                     <input type="number" name="reviewDetail.score" value="${reviewDetail.score!}" min="1" max="100" class="form-control" id="review_score" placeholder="1~100分">
                   </div>
                 </div>
                 <div class="form-group">
                   <label for="review_level" class="col-sm-2 control-label">推荐等级: </label>
                   <div class="col-sm-10">
                     <select class="form-control" id="review_level" name="reviewDetail.level.id">
                       <option value="">请选择推荐等级</option>
                       [#list levels as l]
                       <option value="${l.id}" [#if l.id=(reviewDetail.level.id)!0]selected[/#if]>${l.name}</option>
                       [/#list]
                     </select>
                   </div>
                 </div>
                 <div class="form-group">
                   <label for="review_comments" class="col-sm-2 control-label">评审意见: </label>
                   <div class="col-sm-10">
                     <textarea name="reviewDetail.comments" id="review_comments" class="form-control" rows="4" placeholder="100字以内的评审意见">${reviewDetail.comments!}</textarea>
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
   function check_review(form){
     if(!form["reviewDetail.level.id"].value){
       alert("请选择该项目的推荐级别");
       return false;
     }
     if(!form['reviewDetail.score'].value){
       alert("请填写评分");
       return false;
     }
     if(!form['reviewDetail.comments'].value){
       alert("请填写评审意见");
       return false;
     }
     return true;
   }
</script>
[@b.foot/]
