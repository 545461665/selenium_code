package selenium.framework.dataprovider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * xml文件数据提供类
 *
 *TestLogin.xml
 *
 * <cases>
 * <case name="test_login">
 * <row>
 * <data key="user" type="string" value="18600457156"/>
 * <data key="password" type="string" value=""/>
 * <data key="message" type="string" value="请您填写密码"/>
 * <!--<data key="message" type="array" value="12|32|54" spilt="|"/>-->
 * </row>
 *
 * <row>
 * <data key="user" type="string" value=""/>
 * <data key="password" type="string" value="222222"/>
 * <data key="message" type="string" value="请您填写手机/邮箱/用户名"/>
 * </row>
 * </case>
 * </cases>
 */
public class XmlDataProvider extends DataproviderImpl {

    //用例节点标签
    private static final String CASENODETAG = "case";
    //行节点标签
    private static final String ROWNODETAG = "row";
    //数据节点标签
    private static final String DATANODETAG = "data";
    //类型节点标签
    private static final String TYPETAG = "type";
    //值节点标签
    private static final String VALUETAG = "value";
    //分割
    private static final String SPLITTAG = "split";

    /**
     * 重写方法generateData
     *
     * @param caseName 测试用例文件名
     * @param dataFile 数据文件
     */
    protected void generateData(String caseName, String dataFile) {
        NodeList caseList = getNodeList(dataFile);
        if (caseList.equals(null) || caseList.getLength() == 0) {
            return;
        }
        for (int i = 0; i < caseList.getLength(); i++) {
            //获取节点集合的节点
            Node caseNode = caseList.item(i);
            //判断caseNode是否是Node类型，xml文件的节点是否=case，case的属性name的值是否等于@Test注解的方法名
            if (caseNode.getNodeType() == Node.ELEMENT_NODE && caseNode.getNodeName().equals(CASENODETAG)
                    && caseNode.getAttributes().getNamedItem("name").getNodeValue().equals(caseName)) {

                int ii = 1;

                //循环获取case的子节点的第一个节点，getNextSibling：获取case子节点的下一个兄弟节点
                for (Node n = caseNode.getFirstChild(); n != null; n = n.getNextSibling()) {
                    //判断case的子节点是否是row
                    if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(ROWNODETAG)) {
                        if (ii >= start && ii < max) {
                            List<Object> rowdatas = new ArrayList<Object>();

                            //循环获取row的子节点,getNextSibling：获取row子节点的下一个节点
                            for (Node m = n.getFirstChild(); m != null; m = m.getNextSibling()) {
                                //判断row的自己点是否为data
                                if (m.getNodeType() == Node.ELEMENT_NODE && m.getNodeName().equals(DATANODETAG)) {
                                    //把数据添加到集合中
                                    rowdatas.add(getRowDataByType(m));

                                }
                            }
                            if(rowdatas.size()>0) {
                                data.add(rowdatas.toArray());
                            }
                            rowdatas.clear();
                        }
                        ii++;
                    }
                }
                break;
            }
        }

    }

    /**
     * 读取row的data数据
     * @param node 节点的数据
     * @return
     */
    private Object getRowDataByType(Node node) {
        //获取节点的属性type值
        Node type=node.getAttributes().getNamedItem(TYPETAG);
        //判断xml文件中的row子节点中type的值是否=null
        if(type==null){
            //返回value的值
            return node.getAttributes().getNamedItem("value").getNodeValue().toString();
        }
        //判断type的值忽略大小写是否是int
        if(type.getNodeValue().equalsIgnoreCase("int")){
            //返回value的值
            return Integer.valueOf(node.getAttributes().getNamedItem(VALUETAG).getNodeValue().toString());
        }
        //判断type的值忽略大小写是否是double
        if(type.getNodeValue().equalsIgnoreCase("double")){
            return Double.valueOf(node.getAttributes().getNamedItem(VALUETAG).getNodeValue().toString());
        }
        //判断type的值忽略大小写是否是        //array
        if(type.getNodeValue().equalsIgnoreCase("array")){
            //默认切割符是逗号
            String splitString=",";

            //获取split的属性
            Node split = node.getAttributes().getNamedItem(SPLITTAG);
            if (split!=null){
                splitString=split.getNodeValue().toString();
            }
            return node.getAttributes().getNamedItem(VALUETAG).getNodeValue().toString();
        }
        return node.getAttributes().getNamedItem(VALUETAG).getNodeValue();
    }

    /**
     * 读取xml文件的节点
     *
     * @param dataFile 数据文件
     * @return
     */
    private NodeList getNodeList(String dataFile) {
        DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dombuilder = domfac.newDocumentBuilder();
            InputStream is = new FileInputStream(defaultPath + dataFile);
            Document doc = dombuilder.parse(is);
            Element cases = doc.getDocumentElement();
            return cases.getChildNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}





















