import {SubjectType} from "@/services/common";

export interface TagView {
    id: number
    name: string
}

export interface TagSubject {
    tag: string
    subject_id: number
    subject_type: SubjectType
}

export interface UpdateTagSubjectRequest {
    bind: TagSubject[]
    detach: TagSubject[]
}
