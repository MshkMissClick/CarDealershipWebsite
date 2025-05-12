import React, { useEffect, useState } from 'react';
import { Table, Button, notification, Spin } from 'antd';
import axiosInstance from '../utils/axiosConfig';

interface UserCarIdDto {
    userId: number;
    carId: number;
}

interface Order extends UserCarIdDto {
    id: string;
    customerName: string;
    carBrand: string;
    carModel: string;
    fullCarName: string;
}

interface User {
    id: number;
    name: string;
}

interface Car {
    id: number;
    brand: string; // Изменено с {name: string} на string
    model: string;
}

const OrdersTab: React.FC = () => {
    const [orders, setOrders] = useState<Order[]>([]);
    const [users, setUsers] = useState<User[]>([]);
    const [cars, setCars] = useState<Car[]>([]);
    const [loading, setLoading] = useState(false);

    const fetchData = async () => {
        setLoading(true);
        try {
            const [ordersRes, usersRes, carsRes] = await Promise.all([
                axiosInstance.get<UserCarIdDto[]>('/users/orders'),
                axiosInstance.get<User[]>('/users'),
                axiosInstance.get<Car[]>('/cars')
            ]);

            setUsers(usersRes.data);
            setCars(carsRes.data);

            const enrichedOrders = ordersRes.data.map(dto => {
                const user = usersRes.data.find(u => u.id === dto.userId);
                const car = carsRes.data.find(c => c.id === dto.carId);
                const brandName = car?.brand || ''; // Просто берем brand как строку
                const modelName = car?.model || '';
                const fullCarName = `${brandName} ${modelName}`.trim();

                return {
                    ...dto,
                    id: `${dto.userId}-${dto.carId}`,
                    customerName: user?.name || 'Неизвестный клиент',
                    carBrand: brandName,
                    carModel: modelName,
                    fullCarName: fullCarName || 'Неизвестный автомобиль'
                };
            });

            setOrders(enrichedOrders);
        } catch (error) {
            notification.error({
                message: 'Ошибка загрузки',
                description: 'Не удалось загрузить данные заказов'
            });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleDeleteOrder = async (order: Order) => {
        try {
            await axiosInstance.delete(`/users/${order.userId}/orders/${order.carId}`);
            notification.success({ message: 'Заказ удален!' });
            fetchData();
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось удалить заказ'
            });
        }
    };

    const columns = [
        {
            title: 'Клиент',
            dataIndex: 'customerName',
            key: 'customerName'
        },
        {
            title: 'Автомобиль',
            dataIndex: 'fullCarName',
            key: 'fullCarName',
            render: (text: string, record: Order) => (
                <span>
                    {record.carBrand && <strong>{record.carBrand} </strong>}
                    {record.carModel}
                </span>
            )
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_: any, record: Order) => (
                <Button danger onClick={() => handleDeleteOrder(record)}>
                    Удалить
                </Button>
            )
        }
    ];

    return (
        <div>
            <Spin spinning={loading}>
                <Table
                    columns={columns}
                    dataSource={orders}
                    rowKey="id"
                />
            </Spin>
        </div>
    );
};

export default OrdersTab;