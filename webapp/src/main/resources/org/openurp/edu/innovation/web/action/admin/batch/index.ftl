[#ftl]
[@b.head/]
[@b.toolbar title="大创项目年度批次"/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="batchSearchForm" action="!search" target="batchlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="batch.name;名称"/]
      <input type="hidden" name="orderBy" value="name desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="batchlist" href="!search?orderBy=name desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
