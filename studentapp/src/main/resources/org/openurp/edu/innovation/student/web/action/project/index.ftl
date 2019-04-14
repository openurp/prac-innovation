[#ftl]
[@b.head/]
[@b.toolbar title="项目立项"]bar.addBack();[/@]
[@b.messages/]

[#if batches??&&batches?size==1]
[#assign batch=batches?first]
<div class="container">
<div class="jumbotron">
  <h1>立项开始了!</h1>
  <p>根据大学生创新创业训练计划的要求，现组织开展我校${batch.beginOn?string('YYYY')}年度大学生创新训练类项目的立项工作。为保证立项工作的顺利进行，请各项目负责人按照要求，认真做好立项工作。</p>
  <p>
  [#if projects?size>0]
  [#list projects as project]
  [@b.a class="btn btn-primary btn-lg" href="!edit?project.id="+project.id role="button" title=project.title?html]进入我的项目[/@]
  [/#list]
  [#else]
  [@b.a class="btn btn-primary btn-lg" href="!edit?project.batch.id="+batch.id role="button"]我要立项[/@]
  [/#if]
  </p>
</div>
</div>
[#else]
   不在立项时间。
[/#if]
[@b.foot/]
