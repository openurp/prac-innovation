[#ftl]
[@b.head/]
[#include "../review_nav.ftl"/]

<div class="search-container">
    <div class="search-panel" >
    [#assign defaultBatch=batches?first/]
    [@b.form name="projectSearchForm" action="!search" target="closurelist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="review.project.batch.id" label="年度批次" value=defaultBatch items=batches/]
      [@b.textfields names="review.project.code;编号"/]
      [@b.textfields names="review.project.title;名称"/]
      [@b.textfields names="student;学生"/]
      [@b.select style="width:100px" name="review.project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="review.project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.select style="width:100px" name="has_group" label="分配情况" items={'1':'已分配','0':'未分配'} empty="..." /]
      [@b.textfields names="expert;评审专家"/]
      <input type="hidden" name="orderBy" value="review.project.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="closurelist" href="!search?orderBy=review.project.code asc&review.project.batch.id="+defaultBatch.id/]
    </div>
  </div>
[@b.foot/]
