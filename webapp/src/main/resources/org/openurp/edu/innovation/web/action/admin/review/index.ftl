[#ftl]
[@b.head/]
[@b.toolbar title="评审明细"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [#assign defaultBatch=batches?first/]

    [@b.form name="projectSearchForm" action="!search" target="reviewlist" title="ui.searchForm" theme="search"]
      [@b.select style="width:100px" name="review.project.batch.id" label="年度批次" value=defaultBatch items=batches/]
      [@b.textfields names="review.project.code;编号"/]
      [@b.textfields names="review.project.title;名称"/]
      [@b.textfields names="student;学生"/]
      [@b.select style="width:100px" name="review.project.category.id" label="项目类型" items=projectCategories empty="..." /]
      [@b.select style="width:100px" name="review.project.department.id" label="所属部门" items=departments empty="..." /]
      [@b.textfields names="review.expert.name;评审专家"/]
      <input type="hidden" name="orderBy" value="review.project.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="reviewlist" href="!search?orderBy=review.project.code asc&review.project.batch.id="+defaultBatch.id/]
    </td>
  </tr>
</table>
[@b.foot/]
