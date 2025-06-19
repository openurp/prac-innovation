[#ftl]
[@b.head/]
[@b.toolbar title="我的项目"]bar.addBack();[/@]
<div class="container-fluid">
[@b.messages/]
[#if batches?? && batches?size==1]
[#assign batch=batches?first]
<div class="container">
<div class="jumbotron">
  <h2>立项开始了<span style="font-size:0.5em">(${initialStage.beginAt?string("MM-dd")}~${initialStage.endAt?string("MM-dd")})</span></h2>
  <p>
 根据学校大学生创新创业训练计划项目活动安排，现启动${batch.beginOn?string('YYYY')}年度校级大学生创新创业训练计划项目的立项申报工作 。
  </p>
  <p>
  [#if initialStage.noticeHref?? && initialStage.noticeHref?length>0]
  <a href="${initialStage.noticeHref}" class="btn btn-info btn-lg" target="_new">查看完整通知</a>
  [/#if]
  [#assign batchProjects =[]/]
  [#list projects as project]
    [#if project.batch==batch]
    [#assign batchProjects = batchProjects + [project]/]
    [/#if]
  [/#list]

  [#if batchProjects?size>0]
    [#list batchProjects as project]
      [@b.a class="btn btn-primary btn-lg" href="!edit?project.id="+project.id role="button" title=project.title?html]进入我的项目[/@]
    [/#list]
  [#else]
     [#list projectCategories?sort_by("code") as category]
       [@b.a class="btn btn-primary btn-lg" href="!edit?project.batch.id="+batch.id+"&project.category.id=" + category.id role="button"]${category_index+1} ${category.name} 立项[/@]
       &nbsp;
     [/#list]
  [/#if]
  </p>
</div>
</div>
[/#if]

[#list projects as project]
[@b.card class="card-info card-outline"]
  [@b.card_header]
    ${project.title}
    [@b.card_tools]
      <a href="#" class="btn btn-tool" data-toggle="tooltip" title="" data-original-title="Previous"><i class="fa fa-chevron-left"></i></a>
      <a href="#" class="btn btn-tool" data-toggle="tooltip" title="" data-original-title="Next"><i class="fa fa-chevron-right"></i></a>
    [/@]
  [/@]
  [#include "info.ftl"/]
[/@]
[/#list]

</div>
[@b.foot/]
