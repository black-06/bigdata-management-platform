import {TagView} from "@/services/tag";

export interface Column {
    id: number
    create_time: number
    update_time: number
    name: string
    description: string
    type: string
    comment: string
    details: string
}

export interface ColumnView extends Column {
    tags?: TagView[]
}