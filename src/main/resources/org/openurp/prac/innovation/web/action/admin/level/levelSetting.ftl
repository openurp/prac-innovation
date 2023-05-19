[#ftl]
[@b.head/]
[@b.toolbar title="修改项目等级"]bar.addBack();[/@]
  [@b.form action="!updateLevel" theme="list"]
    [@b.field label="项目"]
      ${projects?first.title} 等${projects?size}个项目
    [/@]
    [#list projectLevels?sort_by('code') as pl]
    [@b.textfield name="year_level_"+pl.id label=pl.name+"年份" value="" maxlength="4"  required="false" check="match('integer')"/]

    [/#list]
    [@b.formfoot]
      [#list projects as p]
      <input type="hidden" name="project.id" value="${p.id}"/>
      [/#list]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
[/@]

[@b.foot/]
