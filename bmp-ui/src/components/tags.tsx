import React, {useState} from "react";
import {TagSubject, UpdateTagSubjectRequest} from "@/services/tag";
import {Button, Input, Space, Tag} from "antd";
import {CheckOutlined, CloseOutlined, PlusOutlined, TagOutlined} from "@ant-design/icons";
import {Trunc} from "@/utils/string";
import {Subject, SubjectType} from "@/services/common";
import {ColumnView} from "@/services/column";

const InputTag: (props: {
    confirm: (value: string) => void
}) => JSX.Element = (props: {
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
        <Tag
            onClick={() => {
                inputSet(true)
            }}
        ><PlusOutlined/> New Tag </Tag>
}

function Editor(confirm: () => void, cancel: () => void): [component: JSX.Element, edit: boolean] {
    const [edit, editSet] = useState(false)
    return [(
        edit ? (
            <Space>
                <Button onClick={() => {
                    editSet(false)
                    cancel()
                }}>Cancel</Button>
                <Button type="primary" onClick={() => {
                    editSet(false)
                    confirm()
                }}>Save</Button>
            </Space>
        ) : (
            <Button type="primary" onClick={() => editSet(true)}>Edit</Button>
        )
    ), edit]
}

export function TagsSuite(
    subject: Subject,
    confirm: (req: UpdateTagSubjectRequest) => string[],
): [Editor: JSX.Element, Tags: JSX.Element] {
    const [tags, tagsSet] = useState(subject.tags)
    const [bind, bindSet] = useState(Array<string>)
    const [detach, detachSet] = useState(Array<string>)
    const [editor, edit] = Editor(() => {
        const rst = confirm({
            bind: bind.map(tag => {
                return {tag: tag, subject_id: subject.id, subject_type: subject.type}
            }),
            detach: detach.map(tag => {
                return {tag: tag, subject_id: subject.id, subject_type: subject.type}
            })
        })
        tagsSet(rst)
        bindSet([])
        detachSet([])
    }, () => {
        tagsSet(subject.tags)
        bindSet([])
        detachSet([])
    })
    return [editor, (<>
        {
            tags.map(tag => {
                return <Tag
                    key={tag}
                    closable={edit}
                    onClose={(e: React.MouseEvent<HTMLElement>) => {
                        e.preventDefault();
                        tagsSet(tags.filter(t => t != tag))
                        detach.push(tag)
                    }}
                ><TagOutlined/> {Trunc(tag, 50)}</Tag>
            })
        }
    </>)]
}

export function ColumnTagsSuite(
    columns: ColumnView[],
    confirm: (req: UpdateTagSubjectRequest) => ColumnView[]
): [Editor: JSX.Element, Tags: Map<number, JSX.Element>] {
    const tagMap = new Map<number, [string[], React.Dispatch<React.SetStateAction<string[]>>]>()
    const bindMap = new Map<number, [string[], React.Dispatch<React.SetStateAction<string[]>>]>()
    const detachMap = new Map<number, [string[], React.Dispatch<React.SetStateAction<string[]>>]>()

    const [editorElement, edit] = Editor(() => {
        const bindRst: TagSubject[] = []
        bindMap.forEach(([bind, bindSet], id) => {
            bind.forEach(tag => {
                bindRst.push({tag: tag, subject_id: id, subject_type: SubjectType.COLUMN})
            })
            bindSet([])
        })
        const detachRst: TagSubject[] = []
        detachMap.forEach(([detach, detachSet], id) => {
            detach.forEach(tag => {
                detachRst.push({tag: tag, subject_id: id, subject_type: SubjectType.COLUMN,})
            })
            detachSet([])
        })
        columns = confirm({bind: bindRst, detach: detachRst})
    }, () => {
        bindMap.forEach(([, bindSet]) => bindSet([]))
        detachMap.forEach(([, detachSet]) => detachSet([]))
    })

    const tagElements = new Map<number, JSX.Element>()
    columns.forEach(column => {
        const [tags, tagsSet] = useState(column.tags)
        tagMap.set(column.id, [tags, tagsSet])

        const [bind, bindSet] = useState(Array<string>)
        bindMap.set(column.id, [bind, bindSet])

        const [detach, detachSet] = useState(Array<string>)
        detachMap.set(column.id, [detach, detachSet])

        tagElements.set(column.id, (<>
            {
                tags.map(tag => {
                    return <Tag
                        key={tag}
                        closable={edit}
                        onClose={(e: React.MouseEvent<HTMLElement>) => {
                            e.preventDefault();
                            tagsSet(tags.filter(t => t != tag))
                            detachSet(detach.Add(tag))
                        }}
                    ><TagOutlined/> {Trunc(tag, 50)}</Tag>
                })
            }
            {
                bind.map(tag => {
                    return <Tag
                        key={tag}
                        closable={edit}
                        onClose={(e: React.MouseEvent<HTMLElement>) => {
                            e.preventDefault();
                            bindSet(bind.filter(t => t != tag))
                        }}
                    ><TagOutlined/> {Trunc(tag, 50)}</Tag>
                })
            }
            {
                edit && <InputTag confirm={v => bindSet(bind.indexOf(v) === -1 ? [...bind, v] : bind)}/>
            }
        </>))
    })
    return [editorElement, tagElements]
}