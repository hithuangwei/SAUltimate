package datatables

import datatables.data.ColumnOption
import datatables.data.OrderOption
import datatables.data.SearchOption
import net.sf.json.JSONObject


/**
 * Created by guwei on 16-3-6.
 * 并不是grails command object 似乎其对List数据无法处理
 * http://datatables.net/manual/server-side
 */
/*@grails.validation.Validateable(nullable = true)*/

class DataTableCommand {
    Integer draw
    Integer start
    Integer length
    /*@BindUsing({ obj, source ->
        new SearchOption(value: source['search[value]'],regex: source['search[regex]'])
    })*/
    SearchOption search

    /*@BindUsing({ obj, source ->
        def res = new ArrayList<OrderOption>()
        for (int i=0 ;true;i++)
        {
            println 'XXXXXX:'+source["order[$i][column]"]
            if (source["order[$i][column]"] == null) break;
            print "X$i"
            res.add(new OrderOption(column: source["order[$i][column]"],dir: source["order[$i][dir]"]))
        }
        return res
    })*/
    List<OrderOption> order

    /*@BindUsing({ obj, source ->
        def res = new ArrayList<ColumnOption>()
        for (int i=0 ;true;i++)
        {
            if (source["columns[$i][column]"] == null) break;
            print "Y$i"
            res.add(new ColumnOption(
                    data: source["columns[$i][data]"],
                    name: source["columns[$i][name]"],
                    searchable: source["columns[$i][searchable]"],
                    orderable: source["columns[$i][orderable]"],
                    search: new SearchOption(value: source["columns[$i][search][value]"],
                            regex: source["columns[$i][search][regex]"])
            ))
        }
        return res
    })*/
    List<ColumnOption> columns

    DataTableCommand() {
    }

    DataTableCommand(def params) {
        if (params instanceof JSONObject) {//没有类型校验
            this.draw = params['draw']
            this.start = params['start']
            this.length = params['length']
            this.search = new SearchOption(value: params['search']['value'], regex: params['search']['regex'])

            this.order = new ArrayList<OrderOption>()
            this.columns = new ArrayList<ColumnOption>()
            for (int i = 0; i < params['order'].size(); i++) {
                this.order.add(new OrderOption(column: params['order'][i]['column'], dir: params['order'][i]['dir']))
            }
            for (int i = 0; i < params['columns'].size(); i++) {
                String data = params['columns'][i]['data'].replaceAll('\\\\', '')
                String name = params['columns'][i]['name'].replaceAll('\\\\', '')
                this.columns.add(new ColumnOption(
                        data: data,
                        name: name,
                        searchable: params['columns'][i]['searchable'],
                        orderable: params['columns'][i]['orderable'],
                        search: new SearchOption(value: params['columns'][i]['search']['value'],
                                regex: params['columns'][i]['search']['regex'])
                )
                )
            }
        } else if (params instanceof Map<String, String[]>) { //request.getParameterMap()
            this.draw = Integer.valueOf(params['draw'][0])
            this.start = Integer.valueOf(params['start'][0])
            this.length = Integer.valueOf(params['length'][0])
            this.search = new SearchOption(value: params['search[value]'][0], regex: params['search[regex]'][0] == "true")

            this.order = new ArrayList<OrderOption>()
            this.columns = new ArrayList<ColumnOption>()
            for (int i = 0; true; i++) {
                if (params["order[$i][column]"] == null) break;
                int a = Integer.valueOf(params["order[$i][column]"][0]);
                this.order.add(new OrderOption(column: a, dir: params["order[$i][dir]"][0]))
            }
            for (int i = 0; true; i++) {
                if (params["columns[$i][data]"] == null) break;
                String data = params["columns[$i][data]"][0].replaceAll('\\\\', '')
                String name = params["columns[$i][name]"][0].replaceAll('\\\\', '')
                this.columns.add(new ColumnOption(
                        data: data,
                        name: name,
                        searchable: params["columns[$i][searchable]"][0] == "true",
                        orderable: params["columns[$i][orderable]"][0] == "true",
                        search: new SearchOption(value: params["columns[$i][search][value]"][0],
                                regex: params["columns[$i][search][regex]"][0] == "true")
                )
                )
            }
        } else { //action里面的params
            this.draw = params.getInt('draw', 1)
            this.start = params.getInt('start', 0)
            this.length = params.getInt('length', 10)
            this.search = new SearchOption(value: params['search[value]'], regex: params.getBoolean('search[regex]', false))

            this.order = new ArrayList<OrderOption>()
            this.columns = new ArrayList<ColumnOption>()
            for (int i = 0; true; i++) {
                if (params["order[$i][column]"] == null) break;
                this.order.add(new OrderOption(column: params.getInt("order[$i][column]", null), dir: params["order[$i][dir]"]))
            }
            for (int i = 0; true; i++) {
                if (params["columns[$i][data]"] == null) break;
                String data = params["columns[$i][data]"].replaceAll('\\\\', '')
                String name = params["columns[$i][name]"].replaceAll('\\\\', '')
                this.columns.add(new ColumnOption(
                        data: data,
                        name: name,
                        searchable: params.getBoolean("columns[$i][searchable]", false),
                        orderable: params.getBoolean("columns[$i][orderable]", false),
                        search: new SearchOption(value: params["columns[$i][search][value]"],
                                regex: params.getBoolean("columns[$i][search][regex]", false))
                )
                )
            }
        }

    }
}
