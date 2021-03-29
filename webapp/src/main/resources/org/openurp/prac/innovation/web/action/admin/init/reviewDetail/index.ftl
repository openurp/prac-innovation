[#ftl]
[@b.head/]
[#include "../review_nav.ftl"/]
<div class="search-container">
    <div class="search-panel" >
    [#assign defaultBatch=batches?first/]

    [@b.form name="projectSearchForm" action="!search" target="reviewlist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="detail.review.project.batch.id" label="年度批次" value=defaultBatch items=batches/]
      [@b.textfields names="detail.review.project.code;编号"/]
      [@b.textfields names="detail.review.project.title;名称"/]
      [@b.select style="width:100px" name="detail.review.project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="detail.review.project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.textfields names="review.expert.name;评审专家"/]
      <input type="hidden" name="orderBy" value="detail.review.project.code"/>
    [/@]
    </div>
    <div class="search-list">[@b.div id="reviewlist" href="!search?orderBy=detail.review.project.code asc&detail.review.project.batch.id="+defaultBatch.id/]
    </div>
  </div>
[@b.foot/]
