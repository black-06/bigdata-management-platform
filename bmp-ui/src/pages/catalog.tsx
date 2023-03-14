import './catalog.less'

import React from 'react';
import {Card, Descriptions, Layout, Space, Table, theme, Tree} from 'antd';
import {DownOutlined} from '@ant-design/icons';
import type {DataNode} from 'antd/es/tree';
import "@/utils/collection"
import {ColumnView} from "@/services/column";
import {AssetView} from "@/services/asset";
import {AssetType, FileType, toSubjectType} from "@/services/common";
import moment from "moment";
import {ColumnTagsSuite, TagsSuite} from "@/components/tags";
import {ColumnsType} from "antd/es/table";

const treeData: DataNode[] = [
    {
        title: 'parent 1',
        key: '0-0',
        children: [
            {
                title: 'parent 1-0',
                key: '0-0-0',
                children: [
                    {
                        title: 'leaf',
                        key: '0-0-0-0',
                    },
                    {
                        title: 'leaf',
                        key: '0-0-0-1',
                    },
                    {
                        title: 'leaf',
                        key: '0-0-0-2',
                    },
                ],
            },
            {
                title: 'parent 1-1',
                key: '0-0-1',
                children: [
                    {
                        title: 'leaf',
                        key: '0-0-1-0',
                    },
                ],
            },
            {
                title: 'parent 1-2',
                key: '0-0-2',
                children: [
                    {
                        title: 'leaf',
                        key: '0-0-2-0',
                    },
                    {
                        title: 'leaf',
                        key: '0-0-2-1',
                    },
                ],
            },
        ],
    },
];

const {Sider, Content} = Layout;

function formatDate(d: Date): string {
    return moment(d).format("YYYY-MM-DD hh:mm:ss")
}

const CatalogPage: React.FC = () => {
    const asset: AssetView = {
        id: 1,
        create_time: new Date(),
        update_time: new Date(),
        name: "test name",
        description: "test desc",
        datasource_id: 2,
        type: AssetType.DATABASE,
        path: "/test_path",
        file_type: FileType.NO,
        comment: "test comment",
        details: "test details",

        tags: [
            "tag 1", "tag 2", "tag 3", "tag 4", "tag 5",
            "tag 11", "tag 12", "tag 13", "tag 14", "tag 15",
            "tag 21", "tag 22", "tag 23", "tag 24", "tag 25",
            "tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag tag"
        ]
    }

    const columns: ColumnView[] = [
        {
            id: 1,
            create_time: new Date(),
            update_time: new Date(),
            name: "column_1",
            description: "column_desc_1,column_desc_1,column_desc_1,column_desc_1,column_desc_1,column_desc_1,column_desc_1",
            type: "varchar",
            comment: "comment_1",
            details: "details_1",
            tags: ["sss_1", "s2_123"]
        }, {
            id: 2,
            create_time: new Date(),
            update_time: new Date(),
            name: "column_2",
            description: "column_desc_2",
            type: "int",
            comment: "comment_2",
            details: "details_2",
            tags: ["sss_2"]
        }
    ]

    const {token: {colorBgContainer}} = theme.useToken();

    const [editor, tags] = TagsSuite({
        id: asset.id,
        type: toSubjectType(asset.type),
        tags: asset.tags
    }, req => {
        // TODO: fetch

        const detach = new Set<string>()
        req.detach.forEach(value => {
            detach.add(value.tag)
        })

        const tags: string[] = []
        asset.tags.forEach(tag => {
            if (!detach.has(tag)) tags.push(tag)
        })
        req.bind.forEach(value => tags.push(value.tag))
        return tags
    })

    const [columnEditor, columnTags] = ColumnTagsSuite(columns, req => {
        // TODO: fetch
        return columns
    })

    const schema: ColumnsType<ColumnView> = [
        {title: "ID", dataIndex: "id"},
        {title: "Name", dataIndex: "name"},
        {title: "Type", dataIndex: "type"},
        {title: "Description", dataIndex: "description", ellipsis: true},
        {title: "Comment", dataIndex: "comment"},
        {title: "Details", dataIndex: "details"},
        {
            title: "Tags",
            dataIndex: "tags",
            render: (value: any, record: ColumnView) => columnTags.get(record.id)
        },
        {title: "CreateTime", dataIndex: "create_time"},
        {title: "UpdateTime", dataIndex: "update_time"},
    ]

    return (
        <Layout style={{height: "100vh"}}>
            <Sider breakpoint="md" style={{
                backgroundColor: colorBgContainer,
                padding: "10px"
            }}>
                <Tree
                    className="tree"
                    showLine
                    switcherIcon={<DownOutlined/>}
                    defaultExpandedKeys={['0-0-0']}
                    treeData={treeData}
                    blockNode
                />
            </Sider>
            <Content style={{
                margin: "24px"
            }}>
                <Space direction="vertical" size="middle" style={{width: "100%"}}>
                    <Card title={asset.name}>
                        <Descriptions column={2}>
                            <Descriptions.Item label="ID">{asset.id}</Descriptions.Item>
                            <Descriptions.Item label="Type">{asset.type}</Descriptions.Item>
                            <Descriptions.Item label="Path">{asset.path}</Descriptions.Item>
                            <Descriptions.Item label="Description">{asset.description}</Descriptions.Item>
                            <Descriptions.Item label="CreateTime">{formatDate(asset.create_time)}</Descriptions.Item>
                            <Descriptions.Item label="UpdateTime">{formatDate(asset.update_time)}</Descriptions.Item>
                        </Descriptions>
                    </Card>
                    <Card title={"Connector Info"}>
                        <Descriptions column={2}>

                        </Descriptions>
                    </Card>
                    <Card title={"Tag"} extra={editor}>
                        <Space wrap>{tags}</Space>
                    </Card>
                    <Card title={"Fields"} extra={columnEditor}>
                        <Table
                            bordered
                            columns={schema}
                            dataSource={columns}
                            rowKey={"id"}
                            scroll={{x: "100vw"}}
                        >
                        </Table>
                    </Card>
                </Space>
            </Content>
        </Layout>
    );
}

export default CatalogPage;