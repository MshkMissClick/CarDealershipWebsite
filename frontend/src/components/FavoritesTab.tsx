import React, { useEffect, useState } from 'react';
import { Table, Button, notification, Spin } from 'antd';
import axiosInstance from '../utils/axiosConfig';

interface UserCarIdDto {
    userId: number;
    carId: number;
}

interface Favorite extends UserCarIdDto {
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
    brand: string;
    model: string;
}

const FavoritesTab: React.FC = () => {
    const [favorites, setFavorites] = useState<Favorite[]>([]);
    const [users, setUsers] = useState<User[]>([]);
    const [cars, setCars] = useState<Car[]>([]);
    const [loading, setLoading] = useState(false);

    const fetchData = async () => {
        setLoading(true);
        try {
            const [favoritesRes, usersRes, carsRes] = await Promise.all([
                axiosInstance.get<UserCarIdDto[]>('/users/favorites'),
                axiosInstance.get<User[]>('/users'),
                axiosInstance.get<Car[]>('/cars')
            ]);

            setUsers(usersRes.data);
            setCars(carsRes.data);

            const enrichedFavorites = favoritesRes.data.map(dto => {
                const user = usersRes.data.find(u => u.id === dto.userId);
                const car = carsRes.data.find(c => c.id === dto.carId);
                const brandName = car?.brand || '';
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

            setFavorites(enrichedFavorites);
        } catch (error) {
            notification.error({
                message: 'Ошибка загрузки',
                description: 'Не удалось загрузить данные избранного'
            });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleRemoveFavorite = async (favorite: Favorite) => {
        try {
            await axiosInstance.delete(`/users/${favorite.userId}/favorites/${favorite.carId}`);
            notification.success({ message: 'Удалено из избранного!' });
            fetchData();
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось удалить из избранного'
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
            render: (text: string, record: Favorite) => (
                <span>
                    {record.carBrand && <strong>{record.carBrand} </strong>}
                    {record.carModel}
                </span>
            )
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_: any, record: Favorite) => (
                <Button danger onClick={() => handleRemoveFavorite(record)}>
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
                    dataSource={favorites}
                    rowKey="id"
                />
            </Spin>
        </div>
    );
};

export default FavoritesTab;