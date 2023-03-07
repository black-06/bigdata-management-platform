import {SubjectType} from "@/services/common";

export interface TagView {
    id: number
    name: string
}

export interface TagSubject {
    tag_id?: number
    tag_name?: string
    subject_id: number
    subject_type: SubjectType
}

export class UpdateTagSubjectRequest {
    bind: TagSubject[];
    detach: TagSubject[];


    constructor() {
        this.bind = [];
        this.detach = [];
    }

    addBind(bind: TagSubject): UpdateTagSubjectRequest {
        this.bind.push(bind)
        return this
    }

    addDetach(detach: TagSubject): UpdateTagSubjectRequest {
        this.detach.push(detach)
        return this
    }
}
