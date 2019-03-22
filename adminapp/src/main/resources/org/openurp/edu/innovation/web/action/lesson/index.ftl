[#ftl]
[@b.head/]
[@b.toolbar title="课堂活动"/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="lessonSearchForm" action="!search" target="lessonlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="lesson.crn;名称"/]
      [@b.select name="lesson.teacheDepart.id" label="开课院系" value="${(lesson.teacheDepart.id)!}" items=departments empty="..."/]
      <input type="hidden" name="orderBy" value="crn desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="lessonlist" href="!search?orderBy=crn desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
