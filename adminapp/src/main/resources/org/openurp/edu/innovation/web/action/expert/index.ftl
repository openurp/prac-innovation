[#ftl]
[@b.head/]
[@b.toolbar title="评审专家"/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="expertSearchForm" action="!search" target="expertlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="expert.name;名称"/]
      [@b.textfields names="expert.code;账户"/]
      <input type="hidden" name="orderBy" value="name desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="expertlist" href="!search?orderBy=name desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
