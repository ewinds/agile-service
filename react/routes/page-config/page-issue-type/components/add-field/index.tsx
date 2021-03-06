import React, { useState, useEffect } from 'react';
import {
  Form, Select, DataSet, Modal, Axios,
} from 'choerodon-ui/pro/lib';
import { observer } from 'mobx-react-lite';
import { IModalProps } from '@/common/types';
import { pageConfigApi, PageConfigIssueType } from '@/api';
import issueTable from '@/routes/Issue/components/issue-table';
import stores from 'choerodon-ui/pro/lib/stores';
import { usePageIssueTypeStore } from '../../stores';
import PageIssueTypeStore from '../../stores/PageIssueTypeStore';

const { Option } = Select;
interface Props {
  modal?: IModalProps,
  dataSet: DataSet,
  store: PageIssueTypeStore,
  onSubmitLocal: any,
}
interface IPage {
  name: string,
  fieldTypeName: string,
  fieldName: string,
  id: string,
}
const AddFiled: React.FC<Props> = observer(({
  modal, dataSet, store, onSubmitLocal,
}) => {
  const [pageList, setPageList] = useState([] as IPage[]);
  async function handleSubmit() {
    if (dataSet.validate()) {
      const id = dataSet.current?.toData().field;
      store.addNewLocalField(dataSet.current?.toData().field);
      onSubmitLocal(store.allFieldData.get(id), true);
      dataSet.create();
      return true;
    }
    return false;
  }
  useEffect(() => {
    modal?.handleOk(handleSubmit);
  }, []);
  useEffect(() => {
    pageConfigApi.loadUnSelected(store.currentIssueType).then((res) => {
      const currentDataArr = dataSet.toData();
      const deleteRecords = store.getDeleteRecords.map((record) => record.toData());
      const data = res.filter((item) => currentDataArr.length === 1
        || currentDataArr.every((d: any) => d.field !== item.id))
        .map((item) => store.allFieldData.get(item.id));
      setPageList(data.concat(deleteRecords));
    });
    return () => {
      dataSet.current?.reset();
    };
  }, []);
  return (
    <Form record={dataSet.current}>
      <Select name="field">
        {pageList.map((item) => <Option value={item.id}>{item.name || item.fieldName}</Option>)}
      </Select>
    </Form>
  );
});

const openField = (dataSet: DataSet, store: PageIssueTypeStore, onSubmitLocal: any) => {
  Modal.open({
    key: Modal.key(),
    title: '添加已有字段',
    style: {
      width: 340,
    },
    drawer: true,
    children: <AddFiled dataSet={dataSet} store={store} onSubmitLocal={onSubmitLocal} />,
  });
};
export default openField;
