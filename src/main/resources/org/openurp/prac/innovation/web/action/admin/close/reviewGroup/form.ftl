[#ftl]
[@b.head/]
[@b.toolbar title="新增/修改评审组"]bar.addBack();[/@]
  [@b.form theme="list" action=b.rest.save(reviewGroup)]
    [@b.textfield name="reviewGroup.name" label="组名" value="${reviewGroup.name!}" required="true" maxlength="80" style="width:200px;"/]
    [@b.select name="reviewGroup.discipline.id" label="学科" value="${(reviewGroup.discipline.id)!}" style="width:200px;" items=disciplines empty="..." required="true"/]
    [@b.select2 name1st="allExperts=" name2nd="expert.id" label="成员" items1st=experts?sort_by("name") items2nd=reviewGroup.experts style="width:200px;height:100px" required="true"/]
    [@b.formfoot]
      <input name="reviewGroup.batch.id" value="${reviewGroup.batch.id}" type="hidden"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[@b.foot/]
