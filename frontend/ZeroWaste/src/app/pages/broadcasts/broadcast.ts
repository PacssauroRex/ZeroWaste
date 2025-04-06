export interface Broadcast {
  id: number,
  name: string,
  description: string,
  sendType: string
  emails: string[],
  productsIds: number[],
}

export interface CreateBroadcastDTO {
  name: string,
  description: string,
  sendType: String,
  emails: string[],
  productsIds: number[]
}

export interface UpdateBroadcastDTO {
  name: string,
  description: string,
  sendType: String,
  emails: string[],
  productsIds: number[]
}
