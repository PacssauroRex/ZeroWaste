export interface Broadcast {
  id: number,
  name: string,
  sendType: String
}

export interface CreateBroadcastDTO {
  name: string,
  description: string,
  sendType: String,
  emails: string[],
  productsIds: number[]
}
