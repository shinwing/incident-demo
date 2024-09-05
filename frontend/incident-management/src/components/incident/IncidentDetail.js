import React from 'react';
import { Modal, Form, Input, Button, Select } from 'antd';
import axios from 'axios';
import { message } from 'antd';

const { Option } = Select;

const IncidentFormModal = ({ visible, onCancel, onSuccess }) => {
    const [form] = Form.useForm();

    const handleFormSubmit = async (values) => {
        try {
            await axios.post('http://127.0.0.1:8080/api/backend/incidents', {
                ...values,
                token: `token ${Date.now()}` // 生成 token
            });
            message.success('Incident created successfully');
            onSuccess(); // 通知主组件更新数据
            form.resetFields(); // 重置表单
        } catch (error) {
            message.error('Failed to create incident');
        }
    };

    return (
        <Modal
            title="Create New Incident"
            open={visible} // 使用 open 代替 visible
            onCancel={onCancel}
            footer={null}
            width={800}
            styles={{ body: { padding: '20px' } }} // 使用 styles.body 代替 bodyStyle
        >
            <Form
                form={form}
                layout="vertical"
                onFinish={handleFormSubmit}
            >
                <Form.Item
                    name="title"
                    label="Title"
                    rules={[{ required: true, message: 'Please input the title!' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="description"
                    label="Description"
                    rules={[{ required: true, message: 'Please input the description!' }]}
                >
                    <Input.TextArea rows={4} />
                </Form.Item>
                <Form.Item
                    name="incidentType"
                    label="Incident Type"
                    rules={[{ required: true, message: 'Please input the incident type!' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="incidentSubType"
                    label="Incident Sub Type"
                    rules={[{ required: true, message: 'Please input the incident sub type!' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="priority"
                    label="Priority"
                    rules={[{ required: true, message: 'Please select the priority!' }]}
                >
                    <Select>
                        <Option value="LOW">Low</Option>
                        <Option value="MEDIUM">Medium</Option>
                        <Option value="HIGH">High</Option>
                    </Select>
                </Form.Item>
                <Form.Item
                    name="createdBy"
                    label="Created By"
                    rules={[{ required: true, message: 'Please input the creator!' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        Submit
                    </Button>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default IncidentFormModal;
