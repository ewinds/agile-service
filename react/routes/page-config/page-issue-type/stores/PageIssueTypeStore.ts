import {
  observable, action, runInAction, computed,
} from 'mobx';

import { PageConfigIssueType, pageConfigApi } from '@/api';
import { DataSet } from 'choerodon-ui/pro/lib';
import Record from 'choerodon-ui/pro/lib/data-set/Record';
import { IFieldPostDataProps } from '../../components/create-field/CreateField';

export enum PageIssueTypeStoreStatusCode {
  // update = 'update',
  del = 'delete',
  add = 'add',
  // drag = 'drag_update',
  desc = 'description_update',
  none = 'none',
  null = '',
}
interface IDescriptionTempleProps {
  id: string | undefined,
  template: undefined | string,
  originTemplate: undefined | string,
  objectVersionNumber: undefined | number,
  dirty: boolean,
}
export type PageIFieldPostDataProps = IFieldPostDataProps & {
  local?: boolean, fieldName: string,
  edited: boolean, created: boolean, required: boolean, rank?: string,
};
class PageIssueTypeStore {
  constructor(props: { addUnselectedDataSet: DataSet, sortTableDataSet: DataSet }) {
    this.addUnselectedDataSet = props.addUnselectedDataSet;
    this.sortTableDataSet = props.sortTableDataSet;
  }

  @observable sortTableDataSet: DataSet;

  @observable addUnselectedDataSet: DataSet;

  @observable loading: boolean = false;

  @observable allFieldData = observable.map();

  @observable currentIssueType: PageConfigIssueType = PageConfigIssueType.null;

  @observable dataStatusCode: PageIssueTypeStoreStatusCode = PageIssueTypeStoreStatusCode.null;

  @observable deleteIds: Array<string> = [];

  @observable deleteRecords: Array<Record> = []; // 删除列表中初始时所拥有的字段

  @observable addIds: Array<string> = [];

  @observable createdFields: Array<PageIFieldPostDataProps> = [];

  @observable descriptionObj: IDescriptionTempleProps = {
    id: undefined,
    template: undefined,
    originTemplate: undefined,
    objectVersionNumber: undefined,
    dirty: false,
  };

  @action init(issueType: PageConfigIssueType) {
    this.currentIssueType = issueType;
  }

  @action('清空全部数据') destroy() {
    this.clear();
    this.currentIssueType = PageConfigIssueType.null;
  }

  @action('清空编辑数据') clear() {
    this.dataStatusCode = PageIssueTypeStoreStatusCode.null;
    this.deleteIds.length = 0;
    this.descriptionObj = {
      id: undefined,
      template: undefined,
      originTemplate: undefined,
      objectVersionNumber: undefined,
      dirty: false,
    };
    this.addIds.length = 0;
    this.createdFields.length = 0;
  }

  @action addDeleteRecord(record: Record) {
    this.deleteRecords.push(record);
  }

  @computed get getDeleteRecords() {
    return this.deleteRecords.slice();
  }

  @action('增添删除字段') addDeleteId(id: string) {
    this.deleteIds.push(id);
  }

  @action('删除本地字段') deleteLocalField(code: string, id?: string) {
    let index = -1;
    if (id) { // id 存在 则删除已有字段集合
      index = this.addIds.findIndex((item) => item === id);
      index !== -1 && this.addIds.splice(index, 1);
      return;
    }
    index = this.createdFields.findIndex((item) => item.code === code);
    index !== -1 && this.createdFields.splice(index, 1);
  }

  @action('增加已有字段') addNewLocalField(id: string) {
    this.addIds.push(id);
  }

  @action('增添新字段') addCreatedField(data: PageIFieldPostDataProps) {
    this.createdFields.push(data);
  }

  @computed get getAddIds() {
    return this.addIds.slice();
  }

  @computed get getCreatedFields() {
    return this.createdFields.slice();
  }

  @action setLoading(data: boolean) {
    this.loading = data;
  }

  @computed get getLoading() {
    return this.loading;
  }

  @action setDescriptionObj(data: IDescriptionTempleProps) {
    this.descriptionObj = data;
  }

  @action changeTemplate(data: string) {
    const dataStr = JSON.stringify(data);
    if (dataStr === this.descriptionObj.originTemplate) {
      this.descriptionObj.dirty = false;
    } else {
      this.descriptionObj.dirty = !this.descriptionObj.id ? dataStr !== JSON.stringify([{ insert: '\n' }]) : true;
    }
    this.descriptionObj.template = data;
  }

  @computed get getDescriptionObj() {
    return this.descriptionObj;
  }

  @computed get getDeleteIds() {
    return this.deleteIds.slice();
  }

  @action setDataStatusCode(code: PageIssueTypeStoreStatusCode) {
    this.dataStatusCode = code;
  }

  @action('设置数据状态') changeDataStatusCode(code: PageIssueTypeStoreStatusCode) {
    if (this.dataStatusCode === PageIssueTypeStoreStatusCode.del) {
      return;
    }
    // if (code === PageIssueTypeStoreStatusCode.add) {
    //   this.dataStatusCode = code;
    // }
    this.dataStatusCode = code;
  }

  @computed get getDirty() {
    return this.getDataStatusCode !== PageIssueTypeStoreStatusCode.null
      || this.getDescriptionObj.dirty || this.sortTableDataSet.dirty;
  }

  @computed get getDataStatusCode() {
    return this.dataStatusCode;
  }

  @action setCurrentIssueType(issueType: PageConfigIssueType) {
    this.currentIssueType = issueType;
  }

  @computed get getCurrentIssueType() {
    return this.currentIssueType;
  }

  @action loadAllField = () => {
    pageConfigApi.load().then((res: any) => {
      res?.content?.map((item: any) => {
        this.allFieldData.set(item.id, item);
      });
    });
  }

  loadData = () => {
    this.clear();
    this.addUnselectedDataSet.clear();
    // pageIssueTypeStore.setDataStatusCode(PageIssueTypeStoreStatusCode.ready);
    this.setLoading(true);
    pageConfigApi.loadByIssueType(this.getCurrentIssueType).then((res) => {
      this.sortTableDataSet.loadData(res.fields);
      if (res.issueTypeFieldVO) {
        this.setDescriptionObj({
          ...res.issueTypeFieldVO,
          originTemplate: res.issueTypeFieldVO.template,
          dirty: false,
        });
      }
      this.setLoading(false);
    });
  }
}
// export { PageIssueTypeStore };
export default PageIssueTypeStore;
