import {AssetType, FileType, GetRequest, Result} from "@/services/common";
import {TagView} from "@/services/tag";

export interface Asset {
    id: number
    create_time: Date
    update_time: Date
    name: string
    description: string
    datasource_id: number
    type: AssetType
    path: string
    file_type: FileType
    comment: string
    details: string
}

export interface AssetView extends Asset {
    tags?: TagView[]
}

export async function queryAsset(req: GetRequest): Promise<Result<Asset>> {
    return await fetch(`/asset/${req.id}`).then(value => value.json())
}

