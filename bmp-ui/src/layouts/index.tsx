import {history, Link, Outlet, useLocation} from 'umi';
import React, {useState} from 'react';
import {FolderOpenOutlined, LinkOutlined,} from '@ant-design/icons';
import {Layout, Menu} from 'antd';
import './index.less'
import classNames from "classnames";

const {Header, Sider} = Layout;

interface Item {
    key: string
    icon: React.ReactNode
    label: React.ReactNode
}

const MyLayout: React.FC = (props: any) => {
    const [collapsed, collapsedSet] = useState(false);
    const onCollapse = (collapsed: boolean) => collapsedSet(() => collapsed)
    const items: Item[] = [
        {
            key: '/Catalog',
            icon: <FolderOpenOutlined/>,
            label: <Link to="/Catalog">Catalog</Link>,
        },
        {
            key: '/Datasource',
            icon: <LinkOutlined/>,
            label: <Link to="/Datasource">Datasource</Link>,
        },
    ]

    // find cur item should be selected
    const location = useLocation()
    const curItem = items.find(item => location.pathname.includes(item.key))
    // set index page
    const index: string = "/Catalog"
    if (location.pathname == "/") {
        history.push(index)
    }

    return (
        <Layout className="layout">
            <Header className="header">
                <div className="logo"/>
                <div className="name">BMP
                </div>
            </Header>
            <Layout>
                <Sider
                    collapsible
                    onCollapse={onCollapse}
                    className="sider"
                >
                    <Menu
                        mode="inline"
                        theme="dark"
                        selectedKeys={[curItem?.key || index]}
                        items={items}
                    />
                </Sider>
                <Layout
                    className={classNames("content-transition", collapsed ? "content-collapsed" : "content")}
                >
                    {Outlet(props)}
                </Layout>
            </Layout>
        </Layout>
    );
};

export default MyLayout;