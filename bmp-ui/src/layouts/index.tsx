import {Link, Outlet} from 'umi';
import React from 'react';
import {FolderOpenOutlined, LinkOutlined,} from '@ant-design/icons';
import {Layout, Menu, theme} from 'antd';
import './index.less'

const {Header, Sider} = Layout;

const MyLayout: React.FC = (props: any) => {
    const {
        token: {colorFillQuaternary},
    } = theme.useToken();

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
                    className="sider"
                >
                    <Menu
                        mode="inline"
                        theme="dark"
                        items={[
                            {
                                key: 'Catalog',
                                icon: <FolderOpenOutlined/>,
                                label: <Link to="/Catalog">Catalog</Link>,
                            },
                            {
                                key: 'Datasource',
                                icon: <LinkOutlined/>,
                                label: <Link to="/Datasource">Datasource</Link>,
                            },
                        ]}
                    />
                </Sider>
                <Layout className="content">
                    {Outlet(props)}
                </Layout>
            </Layout>
        </Layout>
    );
};

export default MyLayout;