// src/App.tsx
import React from 'react';
import { Layout, Menu, Tabs } from 'antd';
import CarsTab from './components/CarsTab';
import OrdersTab from './components/OrdersTab'; // Аналогичный компонент для заказов
import FavoritesTab from './components/FavoritesTab'; // Аналогичный компонент для избранных
const { Header, Content } = Layout;

const App: React.FC = () => {
  return (
      <Layout>
        <Header>
          <div style={{ color: 'white', fontSize: 24 }}>Автосалон</div>
        </Header>
        <Content style={{ padding: '20px' }}>
          <Tabs defaultActiveKey="1">
            <Tabs.TabPane tab="Машины" key="1">
              <CarsTab />
            </Tabs.TabPane>
            <Tabs.TabPane tab="Заказы" key="2">
              <OrdersTab />
            </Tabs.TabPane>
            <Tabs.TabPane tab="Избранное" key="3">
              <FavoritesTab />
            </Tabs.TabPane>
          </Tabs>
        </Content>
      </Layout>
  );
};

export default App;
