package script.db.groovy.agile_service

databaseChangeLog(logicalFilePath: 'fd_status_linkage.groovy') {
    changeSet(author: 'ztxemail@163.com', id: '2020-08-17-create-table-status-linkage') {
        createTable(tableName: 'fd_status_linkage') {
            column(name: 'id', type: 'BIGINT UNSIGNED', autoIncrement: 'true', remarks: 'ID,主键') {
                constraints(primaryKey: true)
            }
            column(name: 'issue_type_id', type: 'BIGINT UNSIGNED', remarks: '问题类型Id') {
                constraints(nullable: false)
            }
            column(name: 'project_id', type: 'BIGINT UNSIGNED', remarks: '项目id') {
                constraints(nullable: false)
            }
            column(name: 'status_id', type: 'BIGINT UNSIGNED', remarks: '状态Id') {
                constraints(nullable: false)
            }
            column(name: 'parent_issue_type_code', type: 'VARCHAR(255)', remarks: '父级问题的类型code'){
                constraints(nullable: false)
            }
            column(name: 'parent_issue_status_setting', type: 'BIGINT UNSIGNED', remarks: '父级问题的状态Id'){
                constraints(nullable: false)
            }

            column(name: "object_version_number", type: "BIGINT UNSIGNED", defaultValue: "1")
            column(name: "created_by", type: "BIGINT UNSIGNED", defaultValue: "0")
            column(name: "creation_date", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
            column(name: "last_updated_by", type: "BIGINT UNSIGNED", defaultValue: "0")
            column(name: "last_update_date", type: "DATETIME", defaultValueComputed: "CURRENT_TIMESTAMP")
        }
    }
}