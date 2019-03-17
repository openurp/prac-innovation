[#ftl]
[@b.head/]
[@b.toolbar title="结项管理"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [#assign defaultBatch=batches?first/]
    [#list batches as b]
     [#assign stage=b.getStage(closureStage)!/]
     [#if stage?? && stage.intime]
      [#assign defaultBatch=b/]
     [/#if]
    [/#list]

    [@b.form name="projectSearchForm" action="!search" target="closurelist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="closure.project.batch.id" label="年度批次" value=defaultBatch items=batches/]
      [@b.textfields names="closure.project.code;编号"/]
      [@b.textfields names="closure.project.title;名称"/]
      [@b.textfields names="student;学生"/]
      [@b.select style="width:100px" name="closure.project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="closure.project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.select style="width:100px" name="need_audit" label="免答辩" items={'1':'待审核','0':'已审核'} empty="..." /]
      <input type="hidden" name="orderBy" value="closure.project.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="closurelist" href="!search?orderBy=closure.project.code asc&closure.project.batch.id="+defaultBatch.id/]
    </td>
  </tr>
</table>
[@b.foot/]
