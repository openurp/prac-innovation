[#ftl]
[@b.head/]
[@b.toolbar title="项目管理"/]
<div class="search-container">
    <div class="search-panel" >
    [@b.form name="projectSearchForm" action="!search" target="projectlist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="project.batch.id" label="立项年度" items=batches/]
      [@b.textfields names="project.code;编号"/]
      [@b.textfields names="project.title;名称"/]
      [@b.textfields names="student;学生"/]
      [@b.textfields names="instructor;指导教师"/]
      [@b.select style="width:100px" name="project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.select style="width:100px" name="project.level.id" label="立项级别" items=projectLevels empty="..." /]
      [@b.select style="width:100px" name="project.state.id" label="状态" items=projectStates empty="..." /]

      <input type="hidden" name="orderBy" value="project.code,project.id"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="projectlist" href="!search?orderBy=project.code asc,project.id&project.batch.id="+batches?first.id/]
    </div>
  </div>
[@b.foot/]
