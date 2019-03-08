[#ftl]
[@b.head/]
[@b.toolbar title="项目管理"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="projectSearchForm" action="!search" target="projectlist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="project.batch.id" label="年度批次" items=batches/]
      [@b.textfields names="project.code;编号"/]
      [@b.textfields names="project.title;名称"/]
      [@b.textfields names="student;学生"/]
      [@b.select style="width:100px" name="project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.select style="width:100px" name="project.level.id" label="级别" items=projectLevels empty="..." /]
      <input type="hidden" name="orderBy" value="project.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="projectlist" href="!search?orderBy=project.code asc&project.batch.id="+batches?first.id/]
    </td>
  </tr>
</table>
[@b.foot/]
