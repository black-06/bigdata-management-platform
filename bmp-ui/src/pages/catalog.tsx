import './catalog.less'

import React, {useState} from 'react';
import {Button, Card, Descriptions, Input, Layout, Row, Space, Table, Tag, theme, Tree} from 'antd';
import {CheckOutlined, CloseOutlined, DownOutlined, PlusOutlined, TagOutlined} from '@ant-design/icons';
import type {DataNode} from 'antd/es/tree';
import {TagView, UpdateTagSubjectRequest} from "@/services/tag";
import {Trunc} from "@/utils/string";
import "@/utils/collection"
import {ColumnView} from "@/services/column";
import {AssetView} from "@/services/asset";

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

const Editor: (confirm: () => void) => {
    button: JSX.Element,
    edit: boolean,
} = (
    confirm: () => void
) => {
    const [edit, editSet] = useState(false)
    return {
        button: (
            edit ? (
                <Space>
                    <Button onClick={() => editSet(false)}>Cancel</Button>
                    <Button type="primary" onClick={() => {
                        editSet(false)
                        confirm()
                    }}>Save</Button>
                </Space>
            ) : (
                <Button type="primary" onClick={() => editSet(true)}>Edit</Button>
            )
        ),
        edit: edit
    }
}

const NewInputTag: (props: { confirm: (value: string) => void }) => JSX.Element = (props: {
    confirm: (value: string) => void
}) => {
    const [input, inputSet] = useState(false)
    const [value, valueSet] = useState("");

    return input ? <Input.Group compact>
            <Input
                size="small"
                style={{width: "70%"}}
                type="text"
                onChange={(e) => {
                    valueSet(e.target.value)
                }}
            />
            <Button
                size="small"
                icon={<CheckOutlined style={{fontSize: 9}}/>}
                onClick={() => {
                    inputSet(false)
                    props.confirm(value)
                }}
            />
            <Button
                size="small"
                icon={<CloseOutlined style={{fontSize: 9}}/>}
                onClick={() => {
                    inputSet(false)
                }}
            />
        </Input.Group> :
        <Tag onClick={() => {
            inputSet(true)
        }}><PlusOutlined/> New Tag </Tag>
}

const {Header, Footer, Sider, Content} = Layout;

const CatalogPage: React.FC = () => {
    const asset: AssetView = {}

    const [edit, editSet] = useState(false)

    // TODO: fetch
    const [view, viewSet] = useState([
        {id: 1, name: "tag 1"},
        {id: 2, name: "tag 2"},
        {id: 3, name: "tag 3"},
    ])

    const [req, reqSet] = useState(new UpdateTagSubjectRequest())

    const {
        token: {colorBgContainer},
    } = theme.useToken();

    const tagEditor = Editor(() => {
        // TODO: fetch
    });
    const columnTagEditor = Editor(() => {
        // TODO: fetch
    });


    const columns: ColumnView[] = [
        {
            id: 1,
            create_time: 1,
            update_time: 2,
            name: "column 2",
            description: "",
            type: "varchar",
            comment: "",
            details: "",
            tags: [{id: 1, name: "sss"}]
        }, {
            id: 1,
            create_time: 1,
            update_time: 2,
            name: "column 2",
            description: "",
            type: "varchar",
            comment: "",
            details: "",
            tags: [],
        }
    ]

    const columnRender = (views: TagView[], record: any, index: number) => {
        return columnTagEditor.edit ? (
            <>{
                value.map((view: TagView) => {
                    return <Tag
                        closable
                        onClose={() => detachSet(value => value.Add(view.id))}
                    ><TagOutlined/> {Trunc(view.name, 50)}</Tag>
                })
            }<NewInputTag confirm={(value: string) => {
                viewSet([...value, {id: 0, name: value}])
                bindSet(bind => bind.Add(value))
            }}/>
            </>
        ) : (
            view.map(value => {
                return <Tag><TagOutlined/> {Trunc(value.name, 50)}</Tag>
            })
        )

        columnTagEditor
        console.log(value)
        console.log(record)
        console.log(index)
        return (
            <Row>1</Row>
        )
    }

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
                <Space direction="vertical" size="middle">
                    <Card title={"leaf"}>
                        <Descriptions column={2}>
                            <Descriptions.Item label="ID">1</Descriptions.Item>
                            <Descriptions.Item label="Type">Table</Descriptions.Item>
                            <Descriptions.Item label="Path">parent1 / parent1-0 / leaf</Descriptions.Item>
                            <Descriptions.Item label="Description">this is a desc</Descriptions.Item>
                            <Descriptions.Item label="CreateTime">2021-03-04 18:37:41</Descriptions.Item>
                            <Descriptions.Item label="UpdateTime">2021-03-04 18:37:41</Descriptions.Item>
                        </Descriptions>
                    </Card>
                    <Card title={"Tag"} extra={tagEditor.button}>
                        <Space wrap={true}>{
                            tagEditor.edit ? (
                                <>{
                                    view.map((view: TagView) => {
                                        return <Tag
                                            closable
                                            onClose={() => reqSet(req => req.addDetach({
                                                subject_id: asset.id, subject_type: undefined
                                            }))}
                                        ><TagOutlined/> {Trunc(view.name, 50)}</Tag>
                                    })
                                }<NewInputTag confirm={(value: string) => {
                                    viewSet([...view, {id: 0, name: value}])
                                    bindSet(bind => bind.Add(value))
                                }}/>
                                </>
                            ) : (
                                view.map(value => {
                                    return <Tag><TagOutlined/> {Trunc(value.name, 50)}</Tag>
                                })
                            )
                        }</Space></Card>
                    <Card title={"Fields"} extra={columnTagEditor.button}>
                        <Table
                            bordered
                            columns={[
                                {title: "ID", dataIndex: "id"},
                                {title: "CreateTime", dataIndex: "create_time"},
                                {title: "UpdateTime", dataIndex: "update_time"},
                                {title: "Name", dataIndex: "name"},
                                {title: "Type", dataIndex: "type"},
                                {title: "Description", dataIndex: "description"},
                                {title: "Comment", dataIndex: "comment"},
                                {title: "Details", dataIndex: "details"},
                                {title: "Tags", dataIndex: "tags", render: columnRender},
                            ]}
                            dataSource={columns}
                        >
                        </Table>
                    </Card>
                </Space>
            </Content>
        </Layout>
    );
}

export default CatalogPage;