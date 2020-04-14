[#ftl]
[@b.head/]
[@b.toolbar title="项目级别管理"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [#assign yearMap={}/]
    [#list years as y]
    [#assign yearMap={y?string:y} + yearMap/]
    [/#list]
    [@b.form name="projectSearchForm" action="!search" target="projectlist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="project.batch.id" label="立项年度" items=batches empty="..."/]
      [@b.select style="width:100px" name="year" label="获奖年度" items=yearMap empty="..."/]
      [@b.textfields names="project.code;编号"/]
      [@b.textfields names="project.title;名称"/]
      [@b.textfields names="student;学生"/]
      [@b.textfields names="instructor;指导教师"/]
      [@b.select style="width:100px" name="project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.select style="width:100px" name="project.level.id" label="立项级别" items=projectLevels empty="..." /]
      [@b.select style="width:100px" name="level.id" label="获奖级别" items=projectLevels empty="..." /]
      <input type="hidden" name="orderBy" value="project.code,project.id"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="projectlist" href="!search?orderBy=project.code asc,project.id"/]
    </td>
  </tr>
</table>
[@b.foot/]
