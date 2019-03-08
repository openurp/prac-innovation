[#ftl]
[@b.head/]
[@b.toolbar title="项目结项"]bar.addBack();[/@]
[@b.messages/]
[#list projects as project]
  [#assign stage=project.batch.getStage(closureStage)/]
  [#if stage?? && stage.intime]
      [#assign batch=project.batch/]
  [/#if]
[/#list]
[#if batch??]
<div class="container">
<div class="jumbotron">
  <h1>结项工作开始了!</h1>
  <p>根据大学生创新创业训练计划的要求，现组织开展我校${batch.beginOn?string('YYYY')}年度大学生创新训练类项目的结项工作（创业类项目结项由学生处另行通知）。为保证结项工作的顺利进行，请各项目负责人按照要求，认真做好结项工作。</p>
  <p>
  [#--[@b.a class="btn btn-primary btn-lg" href="!closureForm"]查看通知[/@]--]
  [#list projects as project]
  [@b.a class="btn btn-primary btn-lg" href="!closureForm?project.id="+project.id role="button" title=project.title?html]进入我的项目[/@]
  [/#list]
  </p>
</div>
</div>
[#else]
  [#if projects?size==0]
    没有找到你的项目。
  [#else]
  <div class="alert" role="alert" style="margin:auto;">
  <span>你有${projects?size}个项目，目前不在结项日期内。</span>
  </div>
  [/#if]
[/#if]
[@b.foot/]
