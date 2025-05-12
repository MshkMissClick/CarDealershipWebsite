import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, Select, Tag, notification } from 'antd';
import { HeartOutlined, ShoppingCartOutlined } from '@ant-design/icons';
import axiosInstance from '../utils/axiosConfig';

interface Car {
    id: number;
    brand: { id: number; name: string };
    model: string;
    year: number;
    bodyType: string;
    color: string;
    transmission: string;
    fuelType: string;
    power: number;
    engineVolume: number;
    fuelConsumption: number;
    trunkVolume: number;
    price: number;
    userWhoOrdered?: { id: number; name?: string };
}

interface Brand {
    id: number;
    name: string;
}

const CarsTab: React.FC = () => {
    const [cars, setCars] = useState<Car[]>([]);
    const [brands, setBrands] = useState<Brand[]>([]);
    const [users, setUsers] = useState([]);
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [isOrderModalVisible, setIsOrderModalVisible] = useState(false);
    const [isFavoriteModalVisible, setIsFavoriteModalVisible] = useState(false);
    const [currentCar, setCurrentCar] = useState<Car | null>(null);
    const [selectedCarId, setSelectedCarId] = useState<number | null>(null);
    const [form] = Form.useForm();
    const [orderForm] = Form.useForm();
    const [favoriteForm] = Form.useForm();

    const fetchCars = async () => {
        try {
            const response = await axiosInstance.get('/cars');
            setCars(response.data);
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось загрузить список автомобилей.',
            });
        }
    };

    const fetchBrands = async () => {
        try {
            const response = await axiosInstance.get('/brands');
            setBrands(response.data);
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось загрузить список брендов.',
            });
        }
    };

    const fetchUsers = async () => {
        try {
            const response = await axiosInstance.get('/users');
            setUsers(response.data);
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось загрузить список пользователей.',
            });
        }
    };

    useEffect(() => {
        fetchCars();
        fetchBrands();
        fetchUsers();
    }, []);

    const handleOpenModal = (car?: Car) => {
        if (car) {
            form.setFieldsValue({
                ...car,
                brand: car.brand.name
            });
            setCurrentCar(car);
        } else {
            form.resetFields();
            setCurrentCar(null);
        }
        setIsModalVisible(true);
    };

    const handleCloseModal = () => {
        setIsModalVisible(false);
        form.resetFields();
    };

    const handleSubmit = async (values: any) => {
        try {
            const selectedBrand = brands.find(brand => brand.name === values.brand);
            const carData = {
                ...values,
                brandId: selectedBrand?.id
            };

            if (currentCar) {
                await axiosInstance.patch(`/cars/${currentCar.id}`, carData);
                notification.success({ message: 'Автомобиль обновлен!' });
            } else {
                await axiosInstance.post('/cars', carData);
                notification.success({ message: 'Автомобиль добавлен!' });
            }
            fetchCars();
            handleCloseModal();
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось сохранить данные автомобиля.',
            });
        }
    };

    const handleDelete = async (id: number) => {
        try {
            await axiosInstance.delete(`/cars/${id}`);
            notification.success({ message: 'Автомобиль удален!' });
            fetchCars();
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось удалить автомобиль.',
            });
        }
    };

    const handleOrderCar = (carId: number) => {
        setSelectedCarId(carId);
        setIsOrderModalVisible(true);
    };

    const handleAddToFavorites = (carId: number) => {
        setSelectedCarId(carId);
        setIsFavoriteModalVisible(true);
    };

    const confirmOrder = async (values: { userId: number }) => {
        try {
            await axiosInstance.post(`/users/${values.userId}/orders/${selectedCarId}`);
            notification.success({ message: 'Автомобиль заказан!' });
            fetchCars();
            setIsOrderModalVisible(false);
            orderForm.resetFields();
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось оформить заказ.',
            });
        }
    };

    const confirmFavorite = async (values: { userId: number }) => {
        try {
            await axiosInstance.post(`/users/${values.userId}/favorites/${selectedCarId}`);
            notification.success({ message: 'Автомобиль добавлен в избранное!' });
            setIsFavoriteModalVisible(false);
            favoriteForm.resetFields();
        } catch (error) {
            notification.error({
                message: 'Ошибка',
                description: 'Не удалось добавить в избранное.',
            });
        }
    };

    const columns = [
        {
            title: 'Бренд',
            dataIndex: 'brand',
            key: 'brand',
        },
        {
            title: 'Модель',
            dataIndex: 'model',
            key: 'model',
        },
        {
            title: 'Год',
            dataIndex: 'year',
            key: 'year',
        },
        {
            title: 'Тип кузова',
            dataIndex: 'bodyType',
            key: 'bodyType',
        },
        {
            title: 'Цвет',
            dataIndex: 'color',
            key: 'color',
        },
        {
            title: 'Трансмиссия',
            dataIndex: 'transmission',
            key: 'transmission',
        },
        {
            title: 'Тип топлива',
            dataIndex: 'fuelType',
            key: 'fuelType',
        },
        {
            title: 'Мощность',
            dataIndex: 'power',
            key: 'power',
        },
        {
            title: 'Объем двигателя',
            dataIndex: 'engineVolume',
            key: 'engineVolume',
        },
        {
            title: 'Расход топлива',
            dataIndex: 'fuelConsumption',
            key: 'fuelConsumption',
        },
        {
            title: 'Объем багажника',
            dataIndex: 'trunkVolume',
            key: 'trunkVolume',
        },
        {
            title: 'Статус',
            key: 'status',
            render: (_: any, record: Car) => (
                <Tag color={record.userWhoOrdered ? 'red' : 'green'}>
                    {record.userWhoOrdered ? 'Заказан' : 'Свободен'}
                </Tag>
            ),
        },
        {
            title: 'Цена',
            dataIndex: 'price',
            key: 'price',
            render: (price: number) => `${price}₽`,
        },
        {
            title: 'Действия',
            key: 'actions',
            render: (_: any, record: Car) => (
                <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                    <Button onClick={() => handleOpenModal(record)} type="primary">Редактировать</Button>
                    <Button onClick={() => handleDelete(record.id)} danger>Удалить</Button>
                    <Button
                        icon={<ShoppingCartOutlined />}
                        onClick={() => handleOrderCar(record.id)}
                        disabled={!!record.userWhoOrdered}
                    >
                        {record.userWhoOrdered ? 'Заказан' : 'Заказать'}
                    </Button>
                    <Button
                        icon={<HeartOutlined />}
                        onClick={() => handleAddToFavorites(record.id)}
                    >
                        В избранное
                    </Button>
                </div>
            ),
        },
    ];

    return (
        <div>
            <Button onClick={() => handleOpenModal()} type="primary" style={{ marginBottom: 16 }}>
                Добавить автомобиль
            </Button>
            <Table columns={columns} dataSource={cars} rowKey="id" />

            <Modal
                title={currentCar ? 'Редактировать автомобиль' : 'Добавить автомобиль'}
                visible={isModalVisible}
                onCancel={handleCloseModal}
                footer={null}
                width={800}
            >
                <Form
                    form={form}
                    layout="vertical"
                    initialValues={currentCar || { brand: '', model: '', year: 2025 }}
                    onFinish={handleSubmit}
                >
                    <Form.Item
                        label="Бренд"
                        name="brand"
                        rules={[{ required: true, message: 'Пожалуйста, выберите бренд!' }]}
                    >
                        <Select placeholder="Выберите бренд">
                            {brands.map((brand) => (
                                <Select.Option key={brand.id} value={brand.name}>
                                    {brand.name}
                                </Select.Option>
                            ))}
                        </Select>
                    </Form.Item>

                    <Form.Item
                        label="Модель"
                        name="model"
                        rules={[{ required: true, message: 'Пожалуйста, введите модель!' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="Год"
                        name="year"
                        rules={[{ required: true, message: 'Пожалуйста, введите год!' }]}
                    >
                        <Input type="number" />
                    </Form.Item>

                    <Form.Item
                        label="Тип кузова"
                        name="bodyType"
                        rules={[{ required: true, message: 'Пожалуйста, введите тип кузова!' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="Цвет"
                        name="color"
                        rules={[{ required: true, message: 'Пожалуйста, введите цвет!' }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.Item
                        label="Трансмиссия"
                        name="transmission"
                        rules={[{ required: true, message: 'Пожалуйста, выберите трансмиссию!' }]}
                    >
                        <Select placeholder="Выберите трансмиссию">
                            <Select.Option value="MANUAL">Механика</Select.Option>
                            <Select.Option value="AUTOMATIC">Автомат</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        label="Тип топлива"
                        name="fuelType"
                        rules={[{ required: true, message: 'Пожалуйста, выберите тип топлива!' }]}
                    >
                        <Select placeholder="Выберите тип топлива">
                            <Select.Option value="GASOLINE">Газ</Select.Option>
                            <Select.Option value="DIESEL">Дизель</Select.Option>
                            <Select.Option value="ELECTRIC">Электрический</Select.Option>
                            <Select.Option value="HYBRID">Гибрид</Select.Option>
                        </Select>
                    </Form.Item>

                    <Form.Item
                        label="Мощность"
                        name="power"
                        rules={[{ required: true, message: 'Пожалуйста, введите мощность!' }]}
                    >
                        <Input type="number" />
                    </Form.Item>

                    <Form.Item
                        label="Объем двигателя"
                        name="engineVolume"
                        rules={[{ required: true, message: 'Пожалуйста, введите объем двигателя!' }]}
                    >
                        <Input type="number" />
                    </Form.Item>

                    <Form.Item
                        label="Расход топлива"
                        name="fuelConsumption"
                        rules={[{ required: true, message: 'Пожалуйста, введите расход топлива!' }]}
                    >
                        <Input type="number" />
                    </Form.Item>

                    <Form.Item
                        label="Объем багажника"
                        name="trunkVolume"
                        rules={[{ required: true, message: 'Пожалуйста, введите объем багажника!' }]}
                    >
                        <Input type="number" />
                    </Form.Item>

                    <Form.Item
                        label="Цена"
                        name="price"
                        rules={[{ required: true, message: 'Пожалуйста, введите цену!' }]}
                    >
                        <Input type="number" />
                    </Form.Item>

                    <Form.Item>
                        <Button type="primary" htmlType="submit">
                            {currentCar ? 'Обновить' : 'Добавить'}
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>

            <Modal
                title="Оформить заказ"
                visible={isOrderModalVisible}
                onCancel={() => setIsOrderModalVisible(false)}
                onOk={() => orderForm.submit()}
            >
                <Form form={orderForm} layout="vertical" onFinish={confirmOrder}>
                    <Form.Item
                        name="userId"
                        label="Пользователь"
                        rules={[{ required: true, message: 'Выберите пользователя' }]}
                    >
                        <Select placeholder="Выберите пользователя">
                            {users.map((user: any) => (
                                <Select.Option key={user.id} value={user.id}>
                                    {user.name}
                                </Select.Option>
                            ))}
                        </Select>
                    </Form.Item>
                </Form>
            </Modal>

            <Modal
                title="Добавить в избранное"
                visible={isFavoriteModalVisible}
                onCancel={() => setIsFavoriteModalVisible(false)}
                onOk={() => favoriteForm.submit()}
            >
                <Form form={favoriteForm} layout="vertical" onFinish={confirmFavorite}>
                    <Form.Item
                        name="userId"
                        label="Пользователь"
                        rules={[{ required: true, message: 'Выберите пользователя' }]}
                    >
                        <Select placeholder="Выберите пользователя">
                            {users.map((user: any) => (
                                <Select.Option key={user.id} value={user.id}>
                                    {user.name}
                                </Select.Option>
                            ))}
                        </Select>
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default CarsTab;