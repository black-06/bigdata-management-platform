export interface GetRequest {
    id: number
}

export interface Result<T> {
    code: number
    msg: string
    data: T
}

export enum AssetType {
    DATABASE = 1,
    TABLE = 2,
    FILESET = 3,
}

export function toSubjectType(assetType: AssetType): SubjectType {
    switch (assetType) {
        case AssetType.DATABASE:
            return SubjectType.DATABASE
        case AssetType.TABLE:
            return SubjectType.TABLE
        case AssetType.FILESET:
            return SubjectType.FILESET
        default:
            return SubjectType.ASSET
    }
}


export enum FileType {
    NO,
    CSV,
    JSON,
    PARQUET,
    ORC,
}

export enum SubjectType {
    COLLECTION = 1,
    DATASOURCE = 2,
    ASSET = 3,
    COLUMN = 4,
    DATABASE = 5,
    TABLE = 6,
    FILESET = 7,
}

export interface Subject {
    id: number
    type: SubjectType
    tags: string[]
}