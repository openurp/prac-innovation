[#ftl]
[@b.head/]
[@b.toolbar title="评审分配管理"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [#assign defaultBatch=batches?first/]
    [@b.form name="projectSearchForm" action="!search" target="closurelist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="closure.project.batch.id" label="年度批次" value=defaultBatch items=batches/]
      [@b.textfields names="closure.project.code;编号"/]
      [@b.textfields names="closure.project.title;名称"/]
      [@b.textfields names="student;学生"/]
      [@b.select style="width:100px" name="closure.project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="closure.project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.select style="width:100px" name="has_group" label="分配情况" items={'1':'已分配','0':'未分配'} empty="..." /]
      [@b.textfields names="expert;评审专家"/]
      <input type="hidden" name="orderBy" value="closure.project.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="closurelist" href="!search?orderBy=closure.project.code asc&closure.project.batch.id="+defaultBatch.id/]
    </td>
  </tr>
</table>
[@b.foot/]
