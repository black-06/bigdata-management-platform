export interface Column {
    id: number
    create_time: Date
    update_time: Date
    name: string
    description: string
    type: string
    comment: string
    details: string
}

export interface ColumnView extends Column {
    tags: string[]
}