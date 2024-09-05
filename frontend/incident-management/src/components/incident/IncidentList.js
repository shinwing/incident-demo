import React, { useState, useEffect } from 'react';
import { Table, Modal, Select, Button, Popconfirm } from 'antd';
import axios from 'axios';
import IncidentFormModal from './IncidentDetail'; // 导入新表单组件

const { Column } = Table;
const { Option } = Select;

const IncidentList = () => {
    const [items, setItems] = useState([]);
    const [status, setStatus] = useState('');
    const [page, setPage] = useState(1);
    const [total, setTotal] = useState(0);
    const [fetchListPending, setFetchListPending] = useState(false);
    const [fetchListError, setFetchListError] = useState(null);
    const [selectedIncident, setSelectedIncident] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isFormModalOpen, setIsFormModalOpen] = useState(false);
    const [newStatus, setNewStatus] = useState('');
    const pageSize = 10;

    useEffect(() => {
        fetchData();
    }, [page, status]);

    const fetchData = async () => {
        setFetchListPending(true);
        try {
            const response = await axios.get('http://127.0.0.1:8080/api/backend/incidents', {
                params: {
                    page: page,
                    pageSize: pageSize,
                    status: status
                }
            });
            setItems(response.data.items);
            setTotal(response.data.totalCount);
        } catch (error) {
            setFetchListError(error);
        } finally {
            setFetchListPending(false);
        }
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    };

    const handleStatusChange = (value) => {
        setStatus(value);
        setPage(1);
    };

    const handleRowClick = (record) => {
        setSelectedIncident(record);
        setNewStatus(record.status); // Initialize with current status
        setIsModalOpen(true);
    };

    const handleModalClose = () => {
        setIsModalOpen(false);
    };

    const handleFormModalOpen = () => {
        setIsFormModalOpen(true);
    };

    const handleFormModalClose = () => {
        setIsFormModalOpen(false);
    };

    const handleFormSuccess = () => {
        handleFormModalClose();
        fetchData();
    };

    const handleStatusUpdate = async () => {
        if (selectedIncident) {
            try {
                await axios.put('http://127.0.0.1:8080/api/backend/incidents', {
                    id: selectedIncident.id,
                    status: newStatus,
                });
                setSelectedIncident({ ...selectedIncident, status: newStatus });
                setIsModalOpen(false);
                fetchData();
            } catch (error) {
                setFetchListError(error);
            }
        }
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://127.0.0.1:8080/api/backend/incidents/${id}`);
            fetchData();
        } catch (error) {
            setFetchListError(error);
        }
    };

    if (fetchListError) {
        return <div>{fetchListError.message}</div>;
    }

    if (fetchListPending) return 'loading...';

    const modalColumns = [
        { title: 'Field', dataIndex: 'field', key: 'field' },
        { title: 'Value', dataIndex: 'value', key: 'value' },
    ];

    const modalDataSource = selectedIncident
        ? [
            { field: 'ID', value: selectedIncident.id },
            { field: 'Title', value: selectedIncident.title },
            { field: 'Description', value: selectedIncident.description },
            { field: 'Incident Type', value: selectedIncident.incidentType },
            { field: 'Incident Sub Type', value: selectedIncident.incidentSubType },
            { field: 'Status', value: selectedIncident.status },
            { field: 'Priority', value: selectedIncident.priority },
            { field: 'Created By', value: selectedIncident.createdBy },
        ]
        : [];

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '16px' }}>
                <Select
                    placeholder="Select status"
                    style={{ width: '200px' }}
                    value={status}
                    onChange={handleStatusChange}
                >
                    <Option value="">All</Option>
                    <Option value="OPEN">Open</Option>
                    <Option value="IN_PROGRESS">In Progress</Option>
                    <Option value="CLOSED">Closed</Option>
                </Select>
                <Button type="primary" onClick={handleFormModalOpen}>
                    New Incident
                </Button>
            </div>
            <Table
                dataSource={items}
                rowKey="id"
                loading={fetchListPending}
                pagination={{
                    current: page,
                    onChange: handlePageChange,
                    total: total,
                    pageSize: pageSize
                }}
            >
                <Column
                    title="ID"
                    dataIndex="id"
                    key="id"
                    render={(id, record) => (
                        <a onClick={() => handleRowClick(record)}>{id}</a>
                    )}
                />
                <Column
                    title={<span style={{ whiteSpace: 'nowrap' }}>Title</span>}
                    dataIndex="title"
                    key="title"
                />
                <Column
                    title={<span style={{ whiteSpace: 'nowrap' }}>Description</span>}
                    dataIndex="description"
                    key="description"
                />
                <Column
                    title={<span style={{ whiteSpace: 'nowrap' }}>Incident Type</span>}
                    dataIndex="incidentType"
                    key="incidentType"
                />
                <Column
                    title={<span style={{ whiteSpace: 'nowrap' }}>Incident Sub Type</span>}
                    dataIndex="incidentSubType"
                    key="incidentSubType"
                />
                <Column
                    title={<span style={{ whiteSpace: 'nowrap' }}>Status</span>}
                    dataIndex="status"
                    key="status"
                />
                <Column
                    title={<span style={{ whiteSpace: 'nowrap' }}>Priority</span>}
                    dataIndex="priority"
                    key="priority"
                />
                <Column
                    title={<span style={{ whiteSpace: 'nowrap' }}>Created By</span>}
                    dataIndex="createdBy"
                    key="createdBy"
                />
                <Column
                    title="Actions"
                    key="actions"
                    render={(text, record) => (
                        <Popconfirm
                            title="Are you sure you want to delete this incident?"
                            onConfirm={() => handleDelete(record.id)}
                            okText="Yes"
                            cancelText="No"
                        >
                            <Button
                                type="link"
                                danger
                                onClick={(e) => e.stopPropagation()} // Prevent row click event
                            >
                                Delete
                            </Button>
                        </Popconfirm>
                    )}
                />
            </Table>
            {selectedIncident && (
                <Modal
                    open={isModalOpen}
                    onCancel={handleModalClose}
                    footer={[
                        <Button key="cancel" onClick={handleModalClose}>
                            Cancel
                        </Button>,
                        <Button key="update" type="primary" onClick={handleStatusUpdate}>
                            Update Status
                        </Button>
                    ]}
                    title="Incident Details"
                    width={800}
                    style={{ top: 20 }}
                >
                    <Table
                        dataSource={modalDataSource}
                        columns={modalColumns}
                        pagination={false}
                        showHeader={false}
                    />
                    <div style={{ marginTop: '16px' }}>
                        <span style={{ marginRight: '8px' }}>Change Status:</span>
                        <Select
                            value={newStatus}
                            onChange={(value) => setNewStatus(value)}
                            style={{ width: '200px' }}
                        >
                            <Option value="OPEN">Open</Option>
                            <Option value="IN_PROGRESS">In Progress</Option>
                            <Option value="CLOSED">Closed</Option>
                        </Select>
                    </div>
                </Modal>
            )}
            <IncidentFormModal
                visible={isFormModalOpen}
                onCancel={handleFormModalClose}
                onSuccess={handleFormSuccess}
            />
        </div>
    );
};

export default IncidentList;
